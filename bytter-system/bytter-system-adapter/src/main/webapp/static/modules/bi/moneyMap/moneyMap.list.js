var selectType,form;
bt.use(['form', 'corp', 'money', 'dicts', 'selectType','dateType','capitalMap','date'], function () {
    $ = bt.jquery, form = bt.form();
    var dicts = bt.dicts.init();
    var corp = bt.corp.init();
    selectType = bt.selectType.init();
    var dateType = bt.dateType.init();

    //监听提交
    form.on('submit(search)', function(data) {
        if(data.field.showType == '2'){
            layer.open({
                type: 2,
                area: ['100%', '100%'],
                title: '资金地图',
                maxmin: true,
                content: basePath + "/bi/synthesize/moneyMap/queryByMap",
                end: function () {
                    var date = bt.date.format(new Date,'yyyy-MM-dd hh:mm:ss');
                    $('#mapDate').text(date)
                }
            });
            return
        }

        $.ajax({
            url: basePath + "/bi/synthesize/moneyMap/query", // 提交地址，该方法能够跳转到一个页面
            data: data.field,
            type: "POST",
            async: false,
            success: function(result) {
            	// alert("result="+result);
                $("#tableContainer").removeClass('layui-hide').html(result);
            }
        });
    });


});

function callToCurr(data){
    if(data.value =="1"){
        selectType.get("reducedCurId").attr({'data-disabled':false,'data-required':true});
    }else {
        selectType.get("reducedCurId").val("");
        selectType.get("reducedCurId").attr({'data-disabled':true,'data-required':false});
    }
    form.renderIcon();
    checkConvertCurCallBack();
}

function clearCur(){
    selectType.get("reducedCurId").val("");
}

/**
 * 币种转换校验
 */
function checkConvertCurCallBack() {
    var origCur = selectType.get("curId").val();
    var reducedCurId = selectType.get("reducedCurId").val();
    if (isNotEmpty(origCur) && isNotEmpty(reducedCurId)) {
        $.ajax({
            url: basePath + "/report/deposit/balance/checkConvertCurId",
            type:"post",
            data:{
                "origCurId": origCur,
                "convertCurId": reducedCurId,
                "date": bt.date.format(new Date(), 'yyyy-mm-dd')
            },
            dataType: "json",
            success:function (data) {
                if (!data.success) {
                    parent.layer ? parent.layer.alert(data.msg, {icon: data.success ? 1 : 2}) : layer.alert(data.msg, {icon: data.success ? 1 : 2});
                    selectType.get("reducedCurId").val("");
                }
            }
        });
    }
}

/**
 * 为空判断
 * @param stringData
 * @returns {boolean}
 */
function isEmpty(stringData) {
    if (null == stringData || stringData == "" || stringData == undefined || stringData == " " || stringData == "null") {
        return true;
    } else {
        return false;
    }
}
/**
 * 非空判断
 * @param stringData
 * @returns {boolean}
 */
function isNotEmpty(stringData) {
    return !isEmpty(stringData)
}