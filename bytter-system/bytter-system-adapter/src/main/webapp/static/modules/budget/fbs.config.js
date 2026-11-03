bt.use(['form', 'jquery', 'element', 'moneyType', 'rateType', 'dicts'], function () {
    var $ = bt.jquery, form = bt.form();
    bt.moneyType.init();
    bt.rateType.init();
    bt.element();
    //监听提交
    form.on('submit(configSubmit)', function (data) {
        $.post(basePath + "/sys/config/save", data.field, function (result) {
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, "JSON");
        return false;
    });

    /**
     * 是否有年计划及控制 0-没有 1-有并控制子周期计划 2-有但不控制子周期计划
     * 启用：显示“年计划上报模式”
     * @param data
     */
    ycrlCallBack = function (data) {
        yimodeView();
    }

    /**
     * 是否有季计划及控制 0-没有 1-有并控制子周期计划 2-有但不控制子周期计划
     * 启用：显示“季计划上报模式”
     * 不启用：禁用“年计划上报模式”中的“年分解月”
     * @param data
     */
    qcrlCallBack = function (data) {
        qimodeView();
    }

    /**
     * 是否有月计划及控制 0-没有 1-有并控制子周期计划 2-有但不控制子周期计划
     * 启用：显示“月计划上报模式”
     * @param data
     */
    mcrlCallBack = function (data) {
        mimodeView();
    }

    /**
     * 是否有周计划及控制 0-没有 1-有并控制子周期计划 2-有但不控制子周期计划
     * 启用：显示“周计划上报模式”，显示“周计划是否整月一起上报”
     * 不启用：禁用“月计划上报模式”中的“月滚动+双月周明细”“月分解周”
     * @param data
     */
    wcrlCallBack = function (data) {
        wimodeView();
    }

    /**
     * 是否有天计划及控制 0-没有 1-有
     * 启用：显示“天计划连续上报天数”
     * @param data
     */
    dcrlCallBack = function (data) {
        dimodeView();
    }

    /**
     * 年计划上报模式 0-年固定 1-年分解月
     * 年分解月：显示“年分解月（月是否上报计划）”
     * @param data
     */
    yimodeCallBack = function (data) {
        yimodeView()
    }

    /**
     * 年分解月（月是否上报计划） 0-否 1-是
     * 否：月计划上报模式必须是“月固定”
     * @param data
     */
    yimodeMCallBack = function (data) {
        yimodeView();
    }

    /**
     * 季计划上报模式 1-季固定 2-季滚动
     * 季滚动：显示“季计划滚动模式”
     * @param data
     */
    qimodeCallBack = function (data) {
        qimodeView();
    }

    /**
     * 月计划上报模式 0-月固定 1-月滚动 2-月滚动+双月周明细 3-月分解周
     * 月滚动：显示“月计划滚动模式”
     * @param data
     */
    mimodeCallBack = function (data) {
        mimodeView();
    }


    /**
     * 周计划上报模式 1-周固定 2-周滚动
     * 周滚动：显示“周计划滚动模式”
     * @param data
     */
    wimodeCallBack = function (data) {
        wimodeView();
    }

    /**
     * 天计划是否连续上报 0-否 1-是
     * 是：显示“天计划连续上报天数”
     * @param data
     */
    dimodeOneTimeCallBack = function (data) {
        dimodeView();
    }

    var dicts = bt.dicts.init();

    /**
     * 年计划相关
     */
    function yimodeView(){
        var ycrl = dicts.get("fbs.ycrl").val();
        if(ycrl == "0"){
            $('#yimodeTr').hide();
            $("#yimodeMTr").hide();
        }else{
            $('#yimodeTr').show();
            var yimode = dicts.get("fbs.yimode").val();
            if (yimode == "1") {
                $("#yimodeMTr").show();
            } else {
                dicts.get("fbs.yimodeM").val(1);
                $("#yimodeMTr").hide();
            }

            var yimodeM = dicts.get("fbs.yimodeM").val();
            if (yimodeM == "0") {
                dicts.get("fbs.mimode").attr({
                    'data-disabledValue':'1,2,3',
                    'value':'0'
                });
            } else {
                dicts.get("fbs.mimode").attr({
                    'data-disabledValue':'3'
                });
                var wcrl = dicts.get("fbs.wcrl").val();
                if (wcrl == 1 || wcrl == 2) {
                    $("#fbs_mimode3").show();
                    dicts.get("fbs.mimode").attr({
                        'data-disabledValue':''
                    });
                }
            }
        }
        dicts.render();
    }
    /**
     * 季计划相关
     */
    function qimodeView(){
        var qcrl = dicts.get("fbs.qcrl").val();
        if(qcrl == "0"){
            $('#qimodeTr').hide();
            $("#qimodeRollTr").hide();
            dicts.get("fbs.yimode").attr({
                'data-disabledValue':''
            });
        }else{
            $('#qimodeTr').show();
            dicts.get("fbs.yimode").attr({
                'data-disabledValue':'1',
                'value':'0',
            });

            var qimode = dicts.get("fbs.qimode").val();
            if (qimode == "1") {
                $("#qimodeRollTr").hide();
            } else {
                $("#qimodeRollTr").show();
            }
        }
        dicts.render();
    }
    /**
     * 月计划相关
     */
    function mimodeView(){
        var mcrl = dicts.get("fbs.mcrl").val();
        if(mcrl == "0"){
            $('#mimodeTr').hide();
            $("#mimodeRollTr").hide();
        }else{
            $('#mimodeTr').show();
            var mimode = dicts.get("fbs.mimode").val();
            if (mimode == "1" || mimode == "2") {
                $("#mimodeRollTr").show();
            } else {
                $("#mimodeRollTr").hide();
            }
        }
        dicts.render();
    }
    /**
     * 周计划相关
     */
    function wimodeView(){
        var wcrl = dicts.get("fbs.wcrl").val();
        if(wcrl == "0"){
            $('#wimodeTr').hide();
            $('#wimodeOneTimeTr').hide();
            $("#wimodeRollTr").hide();
            dicts.get("fbs.mimode").attr({
                'data-disabledValue':'3'
            });
        }else{
            $('#wimodeTr').show();
            $('#wimodeOneTimeTr').show();
            dicts.get("fbs.mimode").attr({
                'data-disabledValue':''
            });
            var wimode = dicts.get("fbs.wimode").val();
            if (wimode == "1") {
                $("#wimodeRollTr").hide();
            } else {
                $("#wimodeRollTr").show();
            }
        }
        dicts.render();
    }
    /**
     * 天计划相关
     */
    function dimodeView(){
        var dcrl = dicts.get("fbs.dcrl").val();
        if(dcrl == "0"){
            $('#dimodeOneTimeTr').hide();
            $('#dimodeOneTimeDaysTr').hide();
        }else{
            $('#dimodeOneTimeTr').show();
            var dimodeOneTime = dicts.get("fbs.dimodeOneTime").val();
            if(dimodeOneTime == "0"){
                $('#dimodeOneTimeDaysTr').hide();
            }else{
                $('#dimodeOneTimeDaysTr').show();
            }
        }
        dicts.render();
    }

    setTimeout(function () {
        yimodeView();
        qimodeView();
        mimodeView();
        wimodeView();
        dimodeView();
    }, 100);

    // 说明显示隐藏
    showDesc();
});

function showDesc() {
    var tip_index1 = 0;
    $('#inputBlance-desc').mouseenter(function () {
        tip_index1 = layer.tips(bt.lang('config.fbs.inputBlance.desc'), '#inputBlance-desc', {time: 30000});
    }).mouseleave(function () {
        layer.close(tip_index1);
    });

    var tip_index2 = 0;
    $('#mimode-desc').mouseenter(function () {
        tip_index2 = layer.tips(bt.lang('config.fbs.mimode.desc'), '#mimode-desc', {time: 30000});
    }).mouseleave(function () {
        layer.close(tip_index2);
    });

    var tip_index3 = 0;
    $('#useBalance-desc').mouseenter(function () {
        tip_index3 = layer.tips(bt.lang('config.fbs.useBalance.desc'), '#useBalance-desc', {time: 30000});
    }).mouseleave(function () {
        layer.close(tip_index3);
    });
}

