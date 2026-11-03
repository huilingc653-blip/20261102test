var datatable={},intercept=Number($('#bwType').val()),selectType;
var form;
bt.use(['form','layedit','btdate','selectType','datatable','dicts','element','btImport','corp'], function(){
    $ = bt.jquery,form = bt.form();
    bt.dicts.init();
    bt.corp.init();
    selectType=bt.selectType.init();
    
  //监听新增保存
   form.on('submit(save)', function(data){
	   //是否共享
       if (typeof(data.field.isCommon) == "undefined") {
           data.field.isCommon = 0;
       } else if (data.field.isCommon == "on") {
           data.field.isCommon = 1;
       } else {
           data.field.isCommon = 0;
       }
	   
       
       //是否有效
       if (typeof(data.field.validSign) == "undefined") {
           data.field.validSign = 0;
       } else if (data.field.validSign == "on") {
           data.field.validSign = 1;
       } else {
           data.field.validSign = 0;
       }

       var operator = $('#operator').val();
        var url = "/blackwhite/configure/save/"+operator;
        $.post(basePath + url,data.field,function(result){
            bt.loading.hide();
            parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){
                if(!bt.navtab){
                    parent.layer?parent.layer.closeAll():layer.closeAll();
                }
                bt.navtab.refreshParentTable();//刷新父页面表格
                if(operator=="update"){
                    bt.navtab.closeCurrent();
                }else {
                    location.reload();//重载当前页面
                }
            }
        },'JSON');
    });
   
   
   isCommonBack();
    
});

function isCommonBack(){
	 if($('[data-name="listType"]').find('select').val()=="02"){
//	      $('input[name="isCommon"]').prop("checked",'true');
		 $('#shareSwith').prop('checked',true);
//	      $('[data-name="isCommon"]').eq(0).prop("checked",true);
	      $('input[name="isCommon"]').attr("disabled","disabled");
	 }else{
	      $('#shareSwith').prop('checked',false);
		  $('#shareSwith').removeAttr("disabled");
	 }
	 form.render('checkbox');
}


/**
 * 设置国家
 * @param data
 * @param elem
 * @param bv
 */
function setArea(data,elem,bv){
//    var url = basePath + "/basic/business/syscountrycust/listSysCountryArea/" + data.id;
//    bt.selectType.get("address").attr({"data-value": "", "data-url": url});
//    bt.selectType.render();
}



