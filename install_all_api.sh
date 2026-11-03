#!/bin/bash
# ============================================================
# bt-saas 依赖安装脚本
# 
# 用法:
#   sh install_all_api.sh          仅安装 client API（默认）
#   sh install_all_api.sh --full   全量安装所有服务（含 gateway/job/actuator）
#   sh install_all_api.sh --help   查看帮助
#
# 二阶段结构:
#   Phase 1: 基础设施 + Client API + bytter-workflow
#            bytter-workflow 被多个业务模块引用，且引用 bytter-system-client，
#            必须在 client API 之后安装
#   Phase 2: 全量服务（--full，含所有 domain/adapter/start）
#
# bytter-all-client/pom.xml 自包含基础设施（bytter-parent + bytter-common-base），
# 可独立执行: mvn clean install -f=bytter-all-client/pom.xml
# ============================================================
set -e

# -----------------------------------------------------------
# 颜色输出
# -----------------------------------------------------------
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

# -----------------------------------------------------------
# 参数解析
# -----------------------------------------------------------
FULL_INSTALL=false

case "${1:-}" in
    "")
        # 默认：仅安装 client API
        ;;
    --full)
        FULL_INSTALL=true
        ;;
    --help|-h)
        echo "bt-saas 依赖安装脚本"
        echo ""
        echo "用法: sh install_all_api.sh [OPTION]"
        echo ""
        echo "选项:"
        echo "  （无参数）   仅安装 client API 模块到本地 Maven 仓库"
        echo "  --full      全量安装：client API + 所有服务的完整构件（含 gateway/job/actuator）"
        echo "  --help      显示本帮助信息"
        echo ""
        echo "说明:"
        echo "  - Phase 1: 通过 bytter-all-client 聚合安装基础设施 + 所有 client API + bytter-workflow"
        echo "  - Phase 2: 仅 --full 模式，完整安装每个服务的全部子模块"
        echo "  - 跳过单元测试以加速构建（-Dmaven.test.skip=true）"
        exit 0
        ;;
    *)
        echo -e "${RED}错误: 未知参数 '${1}'${NC}"
        echo "使用 --help 查看用法"
        exit 1
        ;;
esac

# -----------------------------------------------------------
# 用户自定义 Maven settings（指定本地仓库 D:/maven/repository）
# -----------------------------------------------------------
# MVN_SETTINGS="-s D:/code/bank_t6/bt-saas/settings-local.xml"

# -----------------------------------------------------------
# 通用 Maven 安装函数
# -----------------------------------------------------------
mvn_install() {
    local dir=$1
    local desc=$2
    local opts=${3:-""}

    echo -e "${CYAN}[$(date '+%H:%M:%S')] 正在构建 ${desc} (${dir}) ...${NC}"
    mvn clean install -Dmaven.test.skip=true ${MVN_SETTINGS} ${opts} -f="${dir}/pom.xml"
    echo -e "${GREEN}  ✓ ${desc} 构建完成${NC}"
}

# ===========================================================
# Phase 1: 基础设施 + Client API + bytter-workflow
# ===========================================================
echo ""
echo -e "${YELLOW}========================================${NC}"
echo -e "${YELLOW}  Phase 1: 基础设施 + Client API + bytter-workflow 安装${NC}"
echo -e "${YELLOW}========================================${NC}"
PHASE1_START=$(date '+%s')

# 先安装根父 POM（bytter-parent），确保后续所有子模块的 parent 解析不会失败。
# 虽然 bytter-parent 也是 bytter-all-client 的 modules 列表中的第一个模块，
# 但如果 bytter-all-client 的 reactor 因其他模块失败而中断，bytter-parent 可能不会被安装到本地仓库。
# 单独先安装它作为兜底，成本极低（仅 POM 文件）。
echo ""
echo -e "${CYAN}--- 安装根父 POM（bytter-parent） ---${NC}"
mvn install -N -Dmaven.test.skip=true ${MVN_SETTINGS} -f="bytter-parent/pom.xml"
echo -e "${GREEN}  ✓ bytter-parent 安装完成${NC}"

mvn_install "bytter-all-client" "基础设施 + 所有 client API（聚合安装）"

# 安装所有 client 模块的中间父 POM（非递归，仅安装 POM 本身到本地仓库）
# 原因：client 模块的 parent 是各自服务级 POM（如 bytter-finacial-client 的 parent 是 bytter-manager-account），
# 下游模块通过 Maven 仓库解析 client 依赖时，必须先找到这些中间父 POM，否则报 "Could not find artifact xxx:pom"
echo ""
echo -e "${CYAN}--- 安装中间父 POM（仅 POM，不含子模块） ---${NC}"
CLIENT_PARENT_POMS=(
    "bytter-system"
    "bytter-auth-parent"
    "bytter-manager-plan"
    "bytter-manager-account"
    "bytter-prepose-check"
    "bytter-prepose-credential"
    "bytter-prepose-bill"
    "bytter-prepose-erp"
    "bytter-prepose-accountReceivable"
    "bytter-prepose-cashvoucher"
    "bytter-service-fundwarn"
    "bytter-service-increment"
    "bytter-service-payment"
    "bytter-service-report"
    "bytter-service-bdlink"
    "bytter-service-creditRating"
    "bytter-service-ras"
)
for dir in "${CLIENT_PARENT_POMS[@]}"; do
    echo -e "  installing parent POM ${dir} ..."
    mvn install -N -Dmaven.test.skip=true ${MVN_SETTINGS} -f="${dir}/pom.xml"
done
echo -e "${GREEN}  ✓ 中间父 POM 安装完成${NC}"

# bytter-workflow 引用 bytter-system-client，且被多个业务模块引用，
# 必须放在 client API 之后、Phase 2 服务之前安装
mvn_install "bytter-workflow" "bytter-workflow 完整基础包"

PHASE1_END=$(date '+%s')
echo -e "${GREEN}Phase 1 完成，耗时: $((PHASE1_END - PHASE1_START))s${NC}"

# ===========================================================
# Phase 2: 全量服务安装（仅 --full 模式）
# ===========================================================
if [ "$FULL_INSTALL" = false ]; then
    echo ""
    echo -e "${GREEN}========================================${NC}"
    echo -e "${GREEN}  全部完成（仅 client API 模式）${NC}"
    echo -e "${GREEN}  总耗时: $((PHASE1_END - PHASE1_START))s${NC}"
    echo -e "${GREEN}========================================${NC}"
    echo ""
    echo "如需全量安装所有服务，请使用: sh install_all_api.sh --full"
    exit 0
fi

echo ""
echo -e "${YELLOW}========================================${NC}"
echo -e "${YELLOW}  Phase 2: 全量服务安装${NC}"
echo -e "${YELLOW}========================================${NC}"
PHASE2_START=$(date '+%s')

# --- 全量服务列表（按依赖顺序，bytter-workflow 已移至 Phase 1） ---
FULL_SERVICES=(
    "bytter-system"
    "bytter-auth-parent"
)

# 独立 JAR 服务（无子模块）
STANDALONE_JARS=(
    "bytter-gateway"
    "bytter-job"
    "bytter-actuator"
)

# 业务服务（聚合 POM，含 domain/adapter/start 子模块）
BIZ_SERVICES=(
    "bytter-prepose-check"
    "bytter-prepose-credential"
    "bytter-prepose-bill"
    "bytter-prepose-erp"
    "bytter-prepose-accountReceivable"
    "bytter-prepose-cashvoucher"
    "bytter-service-fundwarn"
    "bytter-service-increment"
    "bytter-service-payment"
    "bytter-service-report"
    "bytter-service-bdlink"
    "bytter-service-creditRating"
    "bytter-service-ras"
    "bytter-manager-plan"
    "bytter-manager-account"
)

echo ""
echo -e "${CYAN}--- 平台服务（聚合模块，全量递归安装） ---${NC}"
for dir in "${FULL_SERVICES[@]}"; do
    mvn_install "$dir" "$dir"
done

echo ""
echo -e "${CYAN}--- 独立 JAR 服务 ---${NC}"
for dir in "${STANDALONE_JARS[@]}"; do
    mvn_install "$dir" "$dir"
done

echo ""
echo -e "${CYAN}--- 业务服务（聚合模块，全量递归安装） ---${NC}"
for dir in "${BIZ_SERVICES[@]}"; do
    mvn_install "$dir" "$dir"
done

PHASE2_END=$(date '+%s')

echo ""
echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}  全部完成（全量安装模式）${NC}"
echo -e "${GREEN}========================================${NC}"
echo -e "  Phase 1 (基础设施+Client+Workflow):  $((PHASE1_END - PHASE1_START))s"
echo -e "  Phase 2 (全量服务):        $((PHASE2_END - PHASE2_START))s"
echo -e "  总耗时:                    $((PHASE2_END - PHASE1_START))s"
echo -e "${GREEN}========================================${NC}"
