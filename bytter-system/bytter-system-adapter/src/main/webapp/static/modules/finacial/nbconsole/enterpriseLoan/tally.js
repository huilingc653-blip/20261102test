	var selectTable = null;
	//初始化记账账号
	function initTallyAccs(v1,v2,v3){
		$("#account2").val("");
		$("#accName2").val("");
    	$("#accNameShow2").html("");
		$("#account2").show();
		if(selectTable==null){
			selectTable = bt.selectTableType.init({
		        elem:".acc",
		        url: basePath+"/finacial/enterpriseLoan/getTallyAccs",
		        urldata:{
		        	netId:v1,
		        	corpId:v2,
		        	curId:v3
		        },
		        paging:true,
		        scrollX:'650px',
		        columns:[
		                {"data":"blnctrAcc",title:bt.lang("table.fcAcc.account")},//账号
		   	            {"data":"accName",title:bt.lang("table.accountDTO.accName")},//户名
		   	         	{"data":"subjectName",title:bt.lang("fcInstrate.subjectCode")},//科目
		   	            {"data":"curName",title:bt.lang("table.fcAcc.curId")},//币别
		        ],
		        clickCallback:function(data,$tr,$elem,full){
		       		$("#account2").val(data.blnctrAcc);
		       		$("#accName2").val(data.accName);
		       		$("#accNameShow2").html(data.accName);
					// 加载现金流项目
					loadCashFlowItem(2, data.blnctrAcc, null, null);
		        }
		    });
		}else{
			selectTable.get("account2").setUrl({netId:v1,corpId:v2,curId:v3});
		}
	}
	
	function checkManual(key, type){
		var action = "/finacial/enterpriseLoan/processAccbook";
		$.post(basePath + action, {key:key,type:type},function(result){
			if(typeof result.success == "boolean" && result.success == false){
     			layer.alert(result.msg);
            	return;
     		}
	    },'JSON');
	}
	
	//初始化业务种类ID
	 function initRaceId(v1,v2){
		$.ajax({
    		type: "POST",
    		url: basePath+"/finacial/enterpriseLoan/getRaceId",
    		data: {"netId" : v1,"type" : v2},
    		dataType : "json",
    		success: function(rs){
    			$("#busRaceId").val(rs.obj.id);
    			$("#raceNameShow").html(rs.obj.raceName);
    		}
    	});
	}
	 
	 function selectAcc(){
         if (bt.navtab) {
             bt.navtab.addNavtab(bt.lang('common.button.add'), basePath + "/finacial/enterpriseLoan/addLoanAcc?sendCode=" + $("#origVoucherNo1").val());
             return;
         }
			/*parent.layer.open({
				title: '新增账户',
		        type: 2,
		        area: ['80%', '80%'],
		        maxmin: false,
		        content: basePath + "/finacial/enterpriseLoan/addLoanAcc?sendCode=" + $("#origVoucherNo1").val(),
		        end:function(){
		            //location.reload();
		        }
		    });*/
		}
	 
