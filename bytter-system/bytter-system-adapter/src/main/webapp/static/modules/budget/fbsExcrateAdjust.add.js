var selectType,moneyObj;
bt.use(['form', 'layedit', 'selectType','moneyType'], function () {
    var $ = bt.jquery, form = bt.form();
    var selectData = {};

    //计划汇率回调
    bt.setRate = function (num) {
        if( num != '' && num != null && num != 0){
            updateRateInformation(num)
        }else{
            $('#example').val('');
        }
    }

    moneyObj = bt.moneyType.init();
    var moneyFun = bt.moneyType.getFun();

    //生成示例说明
    function updateRateInformation(num) {

        var referCurId = select.get("referCurId").val();
        var curId = select.get("curId").val();

        if( !referCurId | !curId ){
            return
        }
        var text = '1';

        bt.each(selectData.curId,function (index,data) {
            if(data.id === curId ){
                text += data.curName;
                text += '('+data.englishCode+')';

            }
        })

        text += '='+ moneyFun.format(num,{separator:"",decimal: 4});

        bt.each(selectData.referCurId,function (index,data) {
            if(data.id === referCurId ){
                text += data.curName;
                text += '('+data.englishCode+')';

            }
        })

        $('#example').val(text);

        return
    }

    //原始货币初始化
    bt.initCur = function (data) {
        selectData.curId = data;
    }
    //目标货币初始化
    bt.initreferCur = function (data) {
        selectData.referCurId = data;
    }
    //原始货币回调
    bt.curCallback = function (data) {
        $("#englishCode").val(data.englishcode);
        moneyObj.get('exchangeRate').val('')
        $('#example').val('');
        var referCurId = select.get("referCurId").val();
        if(data.id){
            select.get("referCurId").attr({"data-itemdisabled":data.id,"data-value":referCurId});
        }else{
            select.get("referCurId").attr({"data-itemdisabled":'',"data-displayData":"all","data-value":referCurId});
        }
    };
    //目标货币回调
    bt.referCurCallback = function (data) {
        $("#referEnglishCode").val(data.englishcode);
        moneyObj.get('exchangeRate').val('')
        $('#example').val('');
        var curId = select.get("curId").val();
        if(data.id){
            select.get("curId").attr({"data-itemdisabled":data.id,"data-value":curId});
        }else{
            select.get("curId").attr({"data-itemdisabled":'',"data-displayData":"all","data-value":curId});
        }

    }

    select = bt.selectType.init();
    //监听提交
    form.on('submit(submit)', function (data) {
        var action = "/budget/fbsexcrateadjust/save";
        $.post(basePath + action, data.field, function (result) {
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}, function () {
                parent.layer.closeAll();
                if (result.success) {
                    if (bt.navtab) {
                        bt.navtab.refreshParent();
                        bt.navtab.refreshCurrent()
                    } else {
                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                    }
                }
            })
        }, 'JSON');
        return false;
    });

});

//重置
function resetSet() {
    $("#englishCode").val('');
    $("#referEnglishCode").val('');
    $("#example").val('');
    $("#rmk").val('');
    moneyObj.get('exchangeRate').val('');
    select.get('curId').val('');
    select.get("curId").attr({"data-itemdisabled":'',"data-displayData":"all","data-value":''});
    select.get('referCurId').val('');
    select.get("referCurId").attr({"data-itemdisabled":'',"data-displayData":"all","data-value":''});
}