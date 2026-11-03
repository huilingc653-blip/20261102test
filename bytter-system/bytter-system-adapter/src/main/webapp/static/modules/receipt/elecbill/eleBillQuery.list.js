var corp;
bt.use(['form','datatable','corp','btdate','select','moneyType','dateType','dicts','selectType'], function(){
    var $ = bt.jquery,form = bt.form();
    var moneyType = bt.moneyType.init();
    bt.dateType.init();
    corp = bt.corp.init();
    bt.selectType.init();
    bt.dicts.init();
    //下拉框
    $.each($("div[type='list']"),function(i,item){
        bt.select.init({
            elem: "[field='"+$(item).attr("field")+"']",
            name: $(item).attr("field"),
            defaultVal: $(item).attr("defaultVal"),
            valueField: $(item).attr("dictField"),
            textField: $(item).attr("dictText"),
            search: true,
            disabled: false,
            required: true,
            url: $(item).attr("fieldhref"),
            change:function(data,text){
                //console.log(data+'---'+text);
            }
        });
    });


    form.on('select(billType)',function (data) {//根据电子回单类型控制存款类型是否显示
        var billType=$("#billType").val();
        if(billType=="04"||billType=="05"||billType=="12"||billType=="13"||billType=="14"||billType=="15"){
            $(".depositType").show();
        }else{
            $(".depositType").hide();
        }
        if(billType == "all" || billType == "17"){
            $("#moneyTd1").hide();
            $("#moneyTd2").hide();
        }else{
            $("#moneyTd1").show();
            $("#moneyTd2").show();
        }
    });


    //格式化数字
    $.each($("input[decimal]"),function(i,item){
        if($(item).attr("decimal")!=""){
            //     bt.moneynum.setnum({trigger:$(item),decimal:$(item).attr('decimal')*1});
        }
    });

    //监听提交
    form.on('submit(submit)', function(data){
        genOtherHtml();
        return false;
    });
    /**
     * 生成账户不公共的属性
     * @return
     */
    var genOtherHtml=function(){
        var url=basePath+"/receipt/eleBillQuery/billQuery";
        $.ajax({
            url:url,
            method: 'POST',
            async:false,
            data: $('#searchForm').serialize(),
            success: function (json) {
                $("#tagBillQuery").html("");
                setTimeout(function(){
                    $("#tagBillQuery").html(json);
                },100);
                form.render();
            }
        });
    };

    //  统一处理页面上的所有弹框方法
    window.openLayer = function (url,layerIndex,shade,callback) {
        parent.layer.closeAll();
        var layerObj = {
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content:url
        }
        if( typeof layerIndex !== 'undefined'){
            layerObj.zIndex = layerIndex
        }

        if( typeof shade !== 'undefined'){
            layerObj.shade = shade
        }

        if( typeof callback !== 'undefined'){
            layerObj.end = function () {
                callback()
            }
        }

        parent.layer.open(layerObj);
    }

});

function corpChange(corpIds) {
    var $ = bt.jquery,form = bt.form();
    var url = basePath+"/receipt/eleBillQuery/json";
    var param={
        corpIds:corp.get("corpIds").val(),
        date:(new Date()).getTime()
    };
    $.ajax({url:url,method: 'POST', data: param,dataType: 'json',
        success: function(data){
            $("#billType").empty();
            $("#billType").append("<option value=\"\">"+bt.lang("common.placeholder.select")+"</option>");
            if(data){
                $.each(data,function(index){
                    $("#billType").append("<option value="+data[index].billType+">"+ data[index].billText+"</option>");
                });
            }
            form.render();
        }
    });
}


