bt.use(['form','layedit','selectTree','select','btdate','corp'], function(){
    var $ = bt.jquery,form = bt.form();
    bt.corp.init();
    bt.moneynum.init({elem:$('[data-type="number"]'),decimal:6,minus:true});
    
    $.each($(".selectDemo"),function(i,item){
        bt.select.init({
            elem: "[field='"+$(item).attr("field")+"']",
            name: $(item).attr("field"),
            defaultVal: $(item).attr("defaultVal"),
            valueField: "id",
            textField:"subjectName",
            search: true,
            disabled: false,
            placeholder:{value:'',text:bt.lang("finacial.fcChargeType.notConsider")},
            url:basePath+"/paramconfig/fcsubject/jsonSubjectDate",
            change:function(data){
            	//console.log(data);
            }
        });
    });
    
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
            url: $(item).attr("fieldhref"),
            placeholder:{value:'',text:bt.lang("finacial.fcChargeType.notConsider")}
        });
    });
    
    //监听提交
    form.on('submit(submit)', function(data){
    	
        var action = "/paramconfig/fcbusinessruledefine/save";
        $.post(basePath + action,data.field,function(result){
            if(result.success){
                parent.layer?parent.layer.closeAll():layer.closeAll();
            }
            parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
        return false;
    });
});
