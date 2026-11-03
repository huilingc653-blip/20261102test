bt.use(['form','layedit','lodop','layer','printType'], function(){
    var $ = bt.jquery,form = bt.form();
    // bt.printType.init();

  // 打印文本
  $(".bt-biz-printType").on('click',function () {
      layer.msg(bt.lang('common.msg.printing'),{time: 800}); //正在打印中
      var printObj = bt.lodop.init();
      setTimeout(function () {
        LODOP = printObj.lodop;//获取lodop打印对象
        if (!LODOP) {
          return;
        }
        LODOP.ADD_PRINT_TEXT(21,300,151,30,$('#contractInfoTitle').text() +"\n");
        LODOP.SET_PRINT_STYLEA(0,"FontSize",12);
        LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
        LODOP.SET_PRINT_STYLEA(0,"Horient",2);
        LODOP.ADD_PRINT_TEXT(63,38,677,330,document.getElementById("contractInfo").value);
        LODOP.SET_PRINT_STYLEA(0,"FontSize",12);
        LODOP.SET_PRINT_STYLEA(0,"ItemType",4);
        LODOP.SET_PRINT_STYLEA(0,"Horient",3);
        LODOP.SET_PRINT_STYLEA(0,"Vorient",3);
        LODOP.ADD_PRINT_LINE(53,23,52,725,0,1);
        LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
        LODOP.SET_PRINT_STYLEA(0,"Horient",3);
        LODOP.ADD_PRINT_LINE(414,60,413,725,0,1);
        LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
        LODOP.SET_PRINT_STYLEA(0,"Horient",3);
        LODOP.SET_PRINT_STYLEA(0,"Vorient",1);
        LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
        LODOP.SET_PRINT_STYLEA(0,"Vorient",1);
        LODOP.SET_PRINT_STYLEA(0,"ItemType",2);
        LODOP.SET_PRINT_STYLEA(0,"Horient",1);
        LODOP.SET_PRINT_STYLEA(0,"Vorient",1);
        LODOP.PREVIEW();
      },800)

  })



    //监听提交
    form.on('submit(submit)', function(data){
        var action = "/cms/loan/cmscontract/modify";
        var mask = layer.load(1, {shade: [0.1, '#FFF']});//遮罩层
        $.post(basePath + action,data.field,function(result){
            parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){
            	layer.close(mask);
            	if(!bt.navtab){
                    parent.layer?parent.layer.closeAll():layer.closeAll();
                } else {
                	bt.navtab.refreshParent();
                	bt.navtab.closeCurrent();
                }
            }else{
            	 layer.close(mask);
            }
        },'JSON');
        return false;
    });

    form.on('submit(downDoc)', function(data){
    	var action = "/cms/loan/cmscontract/downDoc";
    	$.get(basePath + action,data.field,function(result){
        },'JSON');
    	return false;
    });

    //关闭窗口
    doClose = function(){
    	if(bt.navtab){
			bt.navtab.closeCurrent();
		}
    }
});
