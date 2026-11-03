var dateType;
var selectType;
var dicts;
bt.use(['form','bttable','btdate','datatable','laypage','dateType','selectType','date','dicts','jquery','page'], function(){
    $ = bt.jquery,form = bt.form();
    dateType=bt.dateType.init();
    selectType=bt.selectType.init();
    dicts=bt.dicts.init();

    bt.empty = function(ms){
        if(typeof(ms) == "undefined" || ms == null || ms == ""|| ms == " "){
            return true;
        }
        return false;
    }

    //监听提交
    form.on('submit(search)', function(data) {
        search();
    });

    //监听重置
    resetCode = function(){
        var inputs = $('.rCode').find('input');
        inputs.each(function(i, item){
            $(this).val("");
        });

        var selects = $('.rCode').find('.bt-biz-selectType,.bt-biz-dict');
        bt.each(selects,function () {
            $(this).find('select').val(' ');
        })
        // form.render('select');
        $("#bankCode").val("");
        $("#corpId").val("");
    }

});

function search(){
    location.href = basePath + "/bond/fcbondInfo/queryBondUse?"+getParam();
}


var getParam = function(){
    var param="";
    var itemType=dicts.get('itemType').val()
    if(bt.empty(itemType)){
        itemType="";
    }
    param += "corpId="+$('#corpId').val()+
        "&itemType="+itemType+
        "&bankId="+$("#bankCode").val();
    return param;
}


function viewUseDtl(curId,corpId,bankCode) {
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('ration.ratUration.view'),basePath + "/bond/fcbondInfo/viewBondUseDtl?curId="
           + curId + "&corpId=" + corpId + "&bankId=" + bankCode );
        return;
    }
    layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bond/fcbondInfo/viewBondUseDtl?curId="
            + curId + "&corpId=" + corpId + "&bankId=" + bankCode,
        end:function(){
        }
    });
}