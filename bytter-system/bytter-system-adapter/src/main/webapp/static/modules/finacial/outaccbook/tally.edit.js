bt.use(['form','jquery','dateType','moneyType','bttable','selectTable'], function(){
    var $ = bt.jquery,form = bt.form();
    //时间
    bt.dateType.init()
    
    //var html = $("#template tbody tr:eq(0)").html();
    var html = $("#editTable tbody tr:eq(0)").html();
    var i = 0;//修改事这个需要根据表格的tr个数来确定
    var addTr = function(){
		var reg = new RegExp("index","g");
		var tds = html.replace(reg,i);
		var reg = new RegExp("template","g");
		var tds = tds.replace(reg,"dtls");
 		var $tr = $('<tr data-id="' + i + '">'+ tds +'</tr>');
 		$('#editTable > tbody > tr:last').after($tr);
 		
 		//下拉列表
 		selectInit(i);
 	    
 	   bt.moneyType.init();
 	   i++;
    }
    
    var selectInit = function(index){
    	bt.selectTable.init({
 			elem: "#accountTable_"+index,
 			searchInput: '#account_'+index,
 	        scrollX:true,
 	        scrollY:true,
 	        url: basePath+"/finacial/outaccbook/tally/getOutAccTypeAccs",
 			columns: [
 			    {data:"blnctrAcc",},
 			    {data:"accName",},
 				{data:"subjectName",}
 			],
 	        clickCallback: function(severData,data){
 	            $("#account_"+index).val(severData.blnctrAcc);
 	            $("#accName_"+index).text(severData.accName);
 	            $("#subject_"+index).text(severData.subjectName);
 	            $("#curCode_"+index).val(severData.curId);
 	           $("#unit_"+index).text(bt.dict("fcSubject.measureUnit", severData.measureUnit));
 	        },
 		});
    }
   
    
    //监听提交
    form.on('submit(submit)', function(data){
        var action = "/finacial/outaccbook/tally/submitTally";
        $.post(basePath + action,data.field,function(result){
            parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){
            	parent.layer.alert(result.msg, {icon: result.success?1:2});
            	bt.navtab.refreshParent();
            	bt.navtab.closeCurrent();
            }
        },'JSON');
        return false;
    });
    form.on('submit(temp)', function(data){
        var action = "/finacial/outaccbook/tally/tempTally";
        $.post(basePath + action,data.field,function(result){
            parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){
            	parent.layer.alert(result.msg, {icon: result.success?1:2});
            	bt.navtab.refreshParent();
            	bt.navtab.closeCurrent();
            }
        },'JSON');
        return false;
    });
    
    //初始化可编辑表格
    var fcOutAccbookId = $("#fcOutAccbookId").val();
    bt.bttable.init({
		elem: "#editTable",
		url: basePath + "/finacial/outaccbook/tally/queryUpdateDTL/"+fcOutAccbookId,
		/*buttons: [
			{
				text: '<i class="layui-icon">&#xe642;</i>'+bt.lang('common.addTr'),
				action: function(e, dt, node, config){
					addTr();
				}
			}
		],*/
		columns:[
			{
				data:"summaryContent",
				render:function(data, row){
					var s = '<input id="summaryContent_'+ row +'" name="dtls['+ row +'].summaryContent" value="'+ data.summaryContent +'" class="layui-input" placeholder="'+bt.lang('table.fcOutAccbook.summaryContent')+'"/>';
					s += '<input type="hidden" name="dtls['+ row +'].lineNo" value="'+ row +'" />';
					s += '<input type="hidden" name="dtls['+ row +'].id" value="'+ data.id +'" />';
					return s;
				}
			},
			{
				data:"acc",
				render:function(data, row){
					var s = '<input id="account_'+ row +'" name="dtls['+ row +'].account" value="'+ data.acc +'" class="layui-input" type="text" placeholder="'+bt.lang('table.fcOutAccbook.acc')+'"/>';
					s += '<table id="accountTable_'+ row +'"><thead><tr><th>账号 </th><th>账户名称</th><th>科目</th></tr></thead></table>';
					return s;
				}
			},
			{
				data:"accName",
				render:function(data, row){
					var s = '<div id="accName_'+ row +'">'+data.accName+'</div>';
					s += '<input type="hidden" id="curCode_'+ row +'" name="dtls['+ row +'].curCode" value="'+ data.curId +'" >';
					return s;
				}
			},
			{
				data:"subjectName",
				render:function(data, row){
					return '<div id="subject_'+ row +'">'+data.subjectName+'</div>';
				}
			},
			{
				data:"unit",
				render:function(data, row){
					var unitStr = data.unit==null?"":data.unit;
					unitStr = bt.dict("fcSubject.measureUnit", unitStr);
					return '<div id="unit_'+ row +'">'+unitStr+'</div>';
				}
			},
			{
				data:"debitMoney",
				render:function(data, row){
					var debitMoneyStr = data.debitMoney==null?"":data.debitMoney;
					return '<input id="debitMoney_'+ row +'" data-name="dtls['+ row +'].debitMoney" value="'+ debitMoneyStr +'" data-decimal="2" class="layui-input bt-biz-moneyType" placeholder="'+bt.lang('table.fcOutAccbook.income')+'" />'
				}
			},
			{
				data:"creditMoney",
				render:function(data, row){
					var creditMoneyStr = data.creditMoney==null?"":data.creditMoney;
					return '<input id="creditMoney_'+ row +'" data-name="dtls['+ row +'].creditMoney" value="'+ creditMoneyStr +'" data-decimal="2" class="layui-input bt-biz-moneyType" placeholder="'+bt.lang('table.fcOutAccbook.expend')+'" />'
				}
			}
			/*,
			{
				data:null,
				render:function(data, row){
					var reInfo='<button type="button" data-type="del" class="layui-btn layui-btn-mini">'+bt.lang("common.button.delete")+'</button>';
					return '';
				}
			}*/
		],
		delRowCallback:function(data,$tr){
	    	$tr.remove();
	    },            
	    tableInitCallback:function(){
	    	$("[id^='account_']").each(function(){
	    		var idStr = $(this).attr("id");
	    		selectInit(idStr.substring(idStr.indexOf('_')+1));
	    	});
	  		bt.moneyType.init();
	    }
    });
    
    
});

function closeWindow(){
	bt.navtab.closeCurrent();
}