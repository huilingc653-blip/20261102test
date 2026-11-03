package com.bytter.biz.limit.service;

import com.bytter.biz.limit.constant.EnterpriseLimitBizTypeSwitch;
import com.bytter.biz.limit.enums.EnterpriseLimitMessageEnum;
import com.bytter.biz.limit.exception.PaymentLimitExceededException;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.enums.CommonMessageEnum;
import com.bytter.saas.management.exchange.api.dto.LimitHitDetailDto;
import com.bytter.saas.management.exchange.api.dto.PaymentLimitContextDto;
import com.bytter.saas.management.exchange.api.dto.PaymentLimitReleaseDto;
import com.bytter.saas.management.exchange.api.dto.PaymentLimitResultDto;
import com.bytter.saas.management.exchange.api.service.sys.IApiPaymentLimitService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

/**
 * 企业端支付限额客户端门面。
 *
 * <p>只负责把付款办理/资金收付/代发工资/费用报销等业务快照转成 Dubbo DTO，并调用内管
 * {@link IApiPaymentLimitService}。规则匹配、台账占用、用量统计、落地审批判断
 * 全部由内管实现，本类不包含任何本地限额计算。</p>
 */
@Service
public class EnterpriseLimitControlService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EnterpriseLimitControlService.class);

    /** 来源业务类型：付款办理，对齐内管 PaymentLimitConstant.SOURCE_BIZ_TYPE_PAY。 */
    public static final String BIZ_TYPE_NIS_BILLHEAD = "0";
    /** 来源业务类型：代发工资，对齐内管 PaymentLimitConstant.SOURCE_BIZ_TYPE_WAGE。 */
    public static final String BIZ_TYPE_NIS_WAGE = "2";
    /** 来源业务类型：资金收付，对齐内管 PaymentLimitConstant.SOURCE_BIZ_TYPE_CASH_FLOW。 */
    public static final String BIZ_TYPE_FEX_CASH_FLOW = "1";
    /** 来源业务类型：费用报销，对齐内管 PaymentLimitConstant.SOURCE_BIZ_TYPE_EXPENSE。 */
    public static final String BIZ_TYPE_NIS_EXPENSE_DETAIL = "3";

    /**
     * 落地限额处理结果（单据字段 LANDING_LIMIT_RESULT，与内管 BT_TR_LIMIT_CASE.HANDLE_RESULT=A/R 对齐）：
     * 命中落地预置时置 {@link #LANDING_RESULT_PENDING}；内管同意置 {@link #LANDING_RESULT_AGREE}；
     * 内管拒绝置 {@link #LANDING_RESULT_REJECT}；未涉及落地（普通收尾）保持 null。
     */
    public static final String LANDING_RESULT_PENDING = "0";
    public static final String LANDING_RESULT_AGREE = "A";
    public static final String LANDING_RESULT_REJECT = "R";

    /**
     * 落地限额处理结果字典组（sys_dict_group.TYPE_GROUP_CODE），码值即上述 0/A/R，文案含中英文。
     * 列表查询统一用 {@code dictService.listMap(LANDING_RESULT_DICT)} 批量取一次，再逐条回填
     * {@code landingLimitResultName}，避免逐行调用字典服务。
     */
    public static final String LANDING_RESULT_DICT = "enterpriseLimit.landingResult";

    @DubboReference(check = false)
    private IApiPaymentLimitService bankPaymentLimitService;

    /**
     * 首次保存/提交时由内管校验支付限额并占用。
     */
    public PaymentLimitResultDto checkAndOccupy(PaymentLimitContextDto context) {
        if (skipLimitControl(context.getSourceBizType())) {
            return PaymentLimitResultDto.success();
        }
        // 将付款快照提交内管，由内管统一匹配规则、校验支付限额并维护占用台账。
        return invoke("checkAndOccupy", formatContext(context),
                () -> bankPaymentLimitService.checkAndOccupy(context));
    }

    /**
     * 修改、批量提交、自动付款等场景由内管核对已有占用。
     */
    public PaymentLimitResultDto verifyOrOccupy(PaymentLimitContextDto context) {
        if (skipLimitControl(context.getSourceBizType())) {
            return PaymentLimitResultDto.success();
        }
        // 委托内管核对来源单据已有占用；按接口契约，无占用时补做首次校验和占用。
        return invoke("verifyOrOccupy", formatContext(context),
                () -> bankPaymentLimitService.verifyOrOccupy(context));
    }

    /**
     * 业务单据删除/打回/释放时释放对应限额占用。
     */
    public PaymentLimitResultDto release(String tenantId, String sourceBizType, String sourceBizId,
                                         String operatorId) {
        if (skipLimitControl(sourceBizType)) {
            return PaymentLimitResultDto.success();
        }
        PaymentLimitReleaseDto request = new PaymentLimitReleaseDto();
        request.setTenantId(tenantId);
        request.setSourceBizType(sourceBizType);
        request.setSourceBizId(sourceBizId);
        request.setOperatorId(operatorId);
        // 将租户和来源单据标识传给内管，释放该单据对应的限额占用。
        return invoke("release", formatReleaseRequest(request),
                () -> bankPaymentLimitService.release(request));
    }

    /**
     * 支付指令成功后由内管把在途用量转为成功用量。
     */
    public PaymentLimitResultDto markSuccess(String tenantId, String sourceBizType, String sourceBizId) {
        if (skipLimitControl(sourceBizType)) {
            return PaymentLimitResultDto.success();
        }
        PaymentLimitReleaseDto request = new PaymentLimitReleaseDto();
        request.setTenantId(tenantId);
        request.setSourceBizType(sourceBizType);
        request.setSourceBizId(sourceBizId);
        // 通知内管将该来源单据的在途用量转为成功用量，支付成功后仍保留期间占用。
        return invoke("markSuccess", formatReleaseRequest(request),
                () -> bankPaymentLimitService.markSuccess(request));
    }

    /**
     * 费用报销、代发工资等批量付款部分成功后，由内管按保留金额缩减并标记成功占用。
     */
    public PaymentLimitResultDto retainSuccessfulAmount(PaymentLimitContextDto context) {
        if (skipLimitControl(context.getSourceBizType())) {
            return PaymentLimitResultDto.success();
        }
        // 将保留的成功金额交给内管，缩减原占用并标记成功，保留部分支付成功的用量。
        return invoke("retainSuccessfulAmount", formatContext(context),
                () -> bankPaymentLimitService.retainSuccessfulAmount(context));
    }

    /**
     * 由内管判断当前付款是否需要发起落地审批。
     */
    public PaymentLimitResultDto<Boolean> isLandingApprovalRequired(PaymentLimitContextDto context) {
        if (skipLimitControl(context.getSourceBizType())) {
            return PaymentLimitResultDto.success(Boolean.FALSE);
        }
        // 内管命中时保存落地处理单；企业端先判断success，再按data决定是否暂停生成付款指令。
        return invoke("isLandingApprovalRequired", formatContext(context),
                () -> bankPaymentLimitService.isLandingApprovalRequired(context));
    }

    /**
     * 由内管检查落地限额；命中时内管创建/提交落地处理单。
     */
    public PaymentLimitResultDto<Boolean> checkLandingAndCreateCase(PaymentLimitContextDto context) {
        if (skipLimitControl(context.getSourceBizType())) {
            return PaymentLimitResultDto.success(Boolean.FALSE);
        }
        // 由内管校验落地限额并在命中时创建/提交审批单，返回值供业务决定是否暂停付款指令生成。
        return invoke("checkLandingAndCreateCase", formatContext(context),
                () -> bankPaymentLimitService.checkLandingAndCreateCase(context));
    }

    /**
     * 企业端交互提交、状态变更等入口检查返回码，失败时使用本地业务异常提示并回滚。
     * 自动付款直接处理返回结果，不调用此方法，避免业务拒绝被当作任务异常。
     */
    public static void requireSuccess(PaymentLimitResultDto<?> result) {
        if (result == null) {
            throw new BusinessException(CommonMessageEnum.COMMON_MESSAGE_UNKNOWN_ERROR);
        }
        if (!result.isSuccess()) {
            EnterpriseLimitMessageEnum messageEnum = EnterpriseLimitMessageEnum.fromCode(result.getCode());
            if (messageEnum != null) {
                if (messageEnum == EnterpriseLimitMessageEnum.LIMIT_CONTROL_AMOUNT_EXCEEDS_RULE
                        && result.getData() instanceof LimitHitDetailDto) {
                    throw new PaymentLimitExceededException(resolveMessageParams(result),
                            (LimitHitDetailDto) result.getData());
                }
                throw new BusinessException(messageEnum, resolveMessageParams(result));
            }
            throw new BusinessException(result.getCode() + ": " + result.getMessage());
        }
    }

    /**
     * 是否跳过指定来源业务的支付/落地限额控制。开关由 {@link EnterpriseLimitBizTypeSwitch} 统一维护，
     * 关闭时该来源业务直接放行：占用/落地判断返回成功（落地返回 false），释放/成功转量不再调用内管。
     */
    private boolean skipLimitControl(String sourceBizType) {
        boolean skip = !EnterpriseLimitBizTypeSwitch.isLimitEnabled(sourceBizType);
        if (skip) {
            LOGGER.info("支付/落地限额控制已跳过该来源业务: sourceBizType={}", sourceBizType);
        }
        return skip;
    }

    /**
     * 优先使用内管返回的结构化参数；兼容升级期间旧 Provider，从中英文方括号中提取参数。
     */
    private static String[] resolveMessageParams(PaymentLimitResultDto<?> result) {
        if (result.getParams() != null) {
            return result.getParams();
        }
        List<String> params = extractBracketParams(result.getMessage(), '【', '】');
        if (params.isEmpty()) {
            params = extractBracketParams(result.getMessage(), '[', ']');
        }
        return params.isEmpty() ? null : params.toArray(new String[0]);
    }

    /**
     * 按指定括号提取内管旧版提示中的一个或多个动态参数。
     */
    private static List<String> extractBracketParams(String message, char left, char right) {
        List<String> params = new ArrayList<>();
        if (message == null) {
            return params;
        }
        int searchFrom = 0;
        while (searchFrom < message.length()) {
            int start = message.indexOf(left, searchFrom);
            if (start < 0) {
                break;
            }
            int end = message.indexOf(right, start + 1);
            if (end < 0) {
                break;
            }
            params.add(message.substring(start + 1, end));
            searchFrom = end + 1;
        }
        return params;
    }

    /**
     * 统一记录支付限额 Dubbo 调用的请求、响应和异常，便于按业务来源定位失败原因。
     */
    private <T> PaymentLimitResultDto<T> invoke(String methodName, String requestSummary,
                                                 Supplier<PaymentLimitResultDto<T>> remoteCall) {
        LOGGER.info("支付限额Dubbo调用请求: method={}, request={}", methodName, requestSummary);
        long startTime = System.currentTimeMillis();
        try {
            PaymentLimitResultDto<T> result = remoteCall.get();
            long costMillis = System.currentTimeMillis() - startTime;
            if (result == null) {
                LOGGER.warn("支付限额Dubbo调用响应为空: method={}, costMs={}, request={}",
                        methodName, costMillis, requestSummary);
            } else if (result.isSuccess()) {
                LOGGER.info("支付限额Dubbo调用响应: method={}, costMs={}, success={}, code={}, message={}, data={}, params={}, request={}",
                        methodName, costMillis, true, result.getCode(), result.getMessage(), result.getData(),
                        Arrays.toString(result.getParams()), requestSummary);
            } else {
                LOGGER.warn("支付限额Dubbo调用业务失败: method={}, costMs={}, success={}, code={}, message={}, data={}, params={}, request={}",
                        methodName, costMillis, false, result.getCode(), result.getMessage(), result.getData(),
                        Arrays.toString(result.getParams()), requestSummary);
            }
            return result;
        } catch (RuntimeException exception) {
            long costMillis = System.currentTimeMillis() - startTime;
            LOGGER.error("支付限额Dubbo调用异常: method=" + methodName + ", costMs=" + costMillis
                    + ", request=" + requestSummary, exception);
            throw exception;
        }
    }

    /**
     * 展开支付限额校验请求字段；账号日志统一仅保留末四位。
     */
    private String formatContext(PaymentLimitContextDto context) {
        if (context == null) {
            return "null";
        }
        return "tenantId=" + context.getTenantId()
                + ", bankOrgId=" + context.getBankOrgId()
                + ", sourceBizType=" + context.getSourceBizType()
                + ", sourceBizId=" + context.getSourceBizId()
                + ", businessNo=" + context.getBusinessNo()
                + ", unitId=" + context.getUnitId()
                + ", accountId=" + context.getAccountId()
                + ", accountMask=" + maskAccount(context.getAccountMask())
                + ", currencyId=" + context.getCurrencyId()
                + ", amount=" + context.getAmount()
                + ", businessDate=" + context.getBusinessDate()
                + ", bizStatus=" + context.getBizStatus()
                + ", operatorId=" + context.getOperatorId()
                + ", operatorName=" + context.getOperatorName();
    }

    /**
     * 展开支付限额释放或成功转量请求字段。
     */
    private String formatReleaseRequest(PaymentLimitReleaseDto request) {
        if (request == null) {
            return "null";
        }
        return "tenantId=" + request.getTenantId()
                + ", sourceBizType=" + request.getSourceBizType()
                + ", sourceBizId=" + request.getSourceBizId()
                + ", operatorId=" + request.getOperatorId();
    }

    /**
     * 银行账号日志脱敏，仅保留末四位用于问题定位。
     */
    private String maskAccount(String account) {
        if (account == null || account.isEmpty()) {
            return account;
        }
        if (account.length() <= 4) {
            return "****";
        }
        return "****" + account.substring(account.length() - 4);
    }
}
