/** 支付限额拦截提示：只展示服务端返回的当前规则快照。 */
var paymentLimitAlert = (function () {
    function escapeHtml(value) {
        return String(value == null ? '' : value).replace(/[&<>"']/g, function (char) {
            return {'&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;'}[char];
        });
    }

    function formatAmount(value) {
        if (value == null || value === '') return value;
        var amount = String(value);
        if (!/^-?\d+(?:\.\d+)?$/.test(amount)) return value;
        var parts = amount.split('.');
        parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ',');
        return parts.join('.');
    }

    function row(key, value) {
        if (value == null || value === '') return '';
        return '<tr><th style="padding:4px 8px;text-align:left;border:1px solid #ddd">'
            + escapeHtml(bt.lang(key)) + '</th><td style="padding:4px 8px;border:1px solid #ddd">'
            + escapeHtml(value) + '</td></tr>';
    }

    /** 管控渠道文案：企业端付款恒为银企直联，缺失时兜底展示，保证弹窗始终带该行。 */
    function channelName(detail) {
        var code = detail && detail.channelCode ? detail.channelCode : 'EBANK';
        return code === 'EBANK' ? bt.lang('tr.limit.channelDirect') : code;
    }

    function tableCell(value, alignRight) {
        return '<td style="padding:6px 8px;border:1px solid #ddd;white-space:nowrap'
            + (alignRight ? ';text-align:right' : '') + '">' + escapeHtml(value) + '</td>';
    }

    function tableHead(key) {
        return '<th style="padding:6px 8px;border:1px solid #ddd;white-space:nowrap;text-align:center;background:#f2f2f2">'
            + escapeHtml(bt.lang(key)) + '</th>';
    }

    function hasValue(details, field) {
        for (var i = 0; i < details.length; i++) {
            if (details[i] && details[i][field] != null && details[i][field] !== '') return true;
        }
        return false;
    }

    function batchTable(details) {
        var showBusinessNo = hasValue(details, 'businessNo');
        var showAmount = hasValue(details, 'limitAmount') || hasValue(details, 'actualAmount')
            || hasValue(details, 'excessAmount');
        var showCount = hasValue(details, 'limitCount') || hasValue(details, 'actualCount')
            || hasValue(details, 'excessCount');
        var heads = showBusinessNo ? tableHead(details[0].businessNoLabelKey) : '';
        heads += tableHead('tr.limit.rule') + tableHead('tr.limit.limitMode');
        if (showAmount) {
            heads += tableHead('tr.limit.limitAmount') + tableHead('tr.limit.actualAmount')
                + tableHead('tr.limit.excessAmount');
        }
        if (showCount) {
            heads += tableHead('tr.limit.limitCount') + tableHead('tr.limit.actualCount')
                + tableHead('tr.limit.excessCount');
        }
        heads += tableHead('tr.limit.excessRatio') + tableHead('tr.limit.channel')
            + tableHead('tr.limit.excessStrategy');

        var rows = '';
        for (var i = 0; i < details.length; i++) {
            var detail = details[i] || {};
            var mode = detail.limitMode ? bt.lang('tr.limit.mode.' + detail.limitMode) : '';
            var channel = channelName(detail);
            rows += '<tr>' + (showBusinessNo ? tableCell(detail.businessNo) : '')
                + tableCell(detail.ruleName || detail.ruleCode) + tableCell(mode);
            if (showAmount) {
                rows += tableCell(formatAmount(detail.limitAmount), true)
                    + tableCell(formatAmount(detail.actualAmount), true)
                    + tableCell(formatAmount(detail.excessAmount), true);
            }
            if (showCount) {
                rows += tableCell(detail.limitCount, true) + tableCell(detail.actualCount, true)
                    + tableCell(detail.excessCount, true);
            }
            rows += tableCell(detail.excessRatio == null ? ''
                : (Number(detail.excessRatio) * 100).toFixed(2) + '%', true)
                + tableCell(channel) + tableCell(bt.lang('tr.limit.excessStrategyReject')) + '</tr>';
        }
        return '<div style="overflow:auto;margin-top:12px"><table style="border-collapse:collapse;width:100%">'
            + '<thead><tr>' + heads + '</tr></thead><tbody>' + rows + '</tbody></table></div>';
    }

    function content(result) {
        var detail = !result.success && result.obj && result.obj.limitType === 'P' ? result.obj : null;
        if (!detail) return result.msg;
        var message = escapeHtml(bt.lang('tr.limit.interceptMessage'));
        var mode = detail.limitMode ? bt.lang('tr.limit.mode.' + detail.limitMode) : '';
        var rows = row('tr.limit.rule', detail.ruleName || detail.ruleCode)
            + row('tr.limit.limitMode', mode)
            + row('tr.limit.limitAmount', formatAmount(detail.limitAmount))
            + row('tr.limit.actualAmount', formatAmount(detail.actualAmount))
            + row('tr.limit.excessAmount', formatAmount(detail.excessAmount))
            + row('tr.limit.limitCount', detail.limitCount)
            + row('tr.limit.actualCount', detail.actualCount)
            + row('tr.limit.excessCount', detail.excessCount)
            + row('tr.limit.excessRatio', detail.excessRatio == null ? null :
                (Number(detail.excessRatio) * 100).toFixed(2) + '%')
            + row('tr.limit.channel', channelName(detail))
            + row('tr.limit.excessStrategy', bt.lang('tr.limit.excessStrategyReject'));
        return message
            + '<div style="margin-top:12px;font-weight:bold">'
            + escapeHtml(bt.lang('tr.limit.interceptDetail')) + '</div>'
            + '<table style="border-collapse:collapse;width:100%;margin-top:4px">'
            + rows + '</table>';
    }

    function alertOptions(result) {
        var detail = !result.success && result.obj && result.obj.limitType === 'P' ? result.obj : null;
        if (!detail) return {icon: result.success ? 1 : 2};
        return {
            icon: 2,
            title: bt.lang('tr.limit.interceptTitle'),
            btn: [bt.lang('common.button.revert')],
            btnAlign: 'c'
        };
    }

    function limitDetails(result) {
        if (!result || result.success || !result.obj) return [];
        var details = Array.isArray(result.obj) ? result.obj : [result.obj];
        var limitDetails = [];
        for (var i = 0; i < details.length; i++) {
            if (details[i] && details[i].limitType === 'P') limitDetails.push(details[i]);
        }
        return limitDetails;
    }

    function isBatchResult(result) {
        return limitDetails(result).length > 0;
    }

    function batchContent(result) {
        var details = limitDetails(result);
        return details.length > 0
            ? escapeHtml(bt.lang('tr.limit.batchInterceptMessage')) + batchTable(details)
            : result.msg;
    }

    function batchAlertOptions() {
        return {
            icon: 2,
            title: bt.lang('tr.limit.interceptTitle'),
            btn: [bt.lang('common.button.revert')],
            btnAlign: 'c',
            area: ['1100px', '530px']
        };
    }

    return {
        content: content,
        alertOptions: alertOptions,
        isBatchResult: isBatchResult,
        batchContent: batchContent,
        batchAlertOptions: batchAlertOptions
    };
})();
