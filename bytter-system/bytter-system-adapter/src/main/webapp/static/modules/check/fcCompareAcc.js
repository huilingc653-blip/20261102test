var cntrCount = 0;
var cntrDebMoney = 0;
var cntrCreMoney = 0;
var bankCount = 0;
var bankDebMoney = 0;
var bankCreMoney = 0;
var cntrTbData = new Array();
var bankTbData = new Array();
var CntrCheckDatas;
var BankCheckDatas;
var dateChangeCallback;
var handOverFlag = false;
bt.use(['form','jquery','datatable','bttable','dateType','selectTableType','btdate','corp','selectType','element','money','moneyType','dicts'], function(){
    $ = bt.jquery,form = bt.form();
    /**判断字符串是否为空*/
    bt.empty = function(ms){
    	if(typeof(ms) == "undefined" || ms == null || ms == ""){
    		return true;
    	}
    	return false;
    }
    //网点变更
    changeNet = function(data){
    	if(!bt.empty($.trim(data.value))){//单位id不为空
    		$.ajax({
        		type: "POST",
        		url: basePath+"/finacial/check/fccomparecenter?getDayCloseDate",
        		data: {"netId":data.value},
        		dataType : "json",
        		success: function(rs){
        			dateTypes.get("transTime").attr("data-max",rs.obj);
        			dateTypes.get("transTime").val(rs.obj);
        			dateTypes.get("transTime").render();

					dateTypes.get("endTimeCn").attr("data-max",rs.obj);
					dateTypes.get("endTimeCn").val(rs.obj);
					dateTypes.get("endTimeCn").render();

					dateTypes.get("endTimeBank").attr("data-max",rs.obj);
					dateTypes.get("endTimeBank").val(rs.obj);
					dateTypes.get("endTimeBank").render();
        		}
        	});
    	}
    	$("#bankAcc").val("");//清空账号
    	selectTableType.get("bankAcc").setUrl({netCode:$.trim(data.value),natureCode:$("#searchNatureId").val()});//重新加载账号
		$("#searchNetId").val($.trim(data.value));
    }
    //账户性质变更
    changeNature = function(data){
		$("#searchNatureId").val($.trim(data.id));
		selectTableType.get("bankAcc").setUrl({netCode:$("#searchNetId").val(),natureCode:$.trim(data.id)});
    }

    $("#doAutoCheck").click(function(data){
        handOverFlag = false;
        $("#submitDiv").find('button#doAutoCheck').addClass("layui-btn-disabled").attr('disabled','disabled');
        $("#submitDiv").find('button#doHandCheck').removeClass("layui-btn-disabled").removeAttr("disabled");

        clearTableSelect();
        return false;
    });

    $("#doHandCheck").click(function(data){
        handOverFlag = true;
        $("#submitDiv").find('button#doHandCheck').addClass("layui-btn-disabled").attr('disabled','disabled');
        $("#submitDiv").find('button#doAutoCheck').removeClass("layui-btn-disabled").removeAttr("disabled");

        clearTableSelect();
        return false;
    });

    /**
     * 清除表格选中数据
     */
    clearTableSelect = function(){
        BankCheckDatas.setSelectedRow(getTableCheckStatusArray(BankCheckDatas.getSelectedData(),false))
        $calCheckTotalData("bankTb",BankCheckDatas.getSelectedData());
        CntrCheckDatas.setSelectedRow(getTableCheckStatusArray(CntrCheckDatas.getSelectedData(),false));
        $calCheckTotalData("cntrTb",BankCheckDatas.getSelectedData());
    }

	var moneyType = bt.moneyType.init();
    var corps = bt.corp.init();
    var dicts = bt.dicts.init();
    var dateTypes = bt.dateType.init()
    var selectTypes = bt.selectType.init();
  //时间
    $.each($("div[type='date']"),function(i,item){
        bt.btdate.init({
            elem: "[field='"+$(item).attr("field")+"']",
            name: $(item).attr("field"),
            start: $(item).attr("defaultVal"),
            format: "YYYY-MM-DD",
            istime: false
        });
    });

    /**加载银行账号*/
    var selectTableType = bt.selectTableType.init({
        elem:".acc",
        url: basePath+"/finacial/check/fccomparecenter/getBankAccByOpType",
        urldata:{netCode:$("#searchNetId").val()},
        paging:true,
        scrollX:'650px',
        columns:[
                {"data":"account",title:bt.lang('finacial.check.centralityDataManager.centralityAcc')},
   	            {"data":"bankAcc",title:bt.lang('table.nisBillhead.bankAcc')},
   	            {"data":"bankName",title:bt.lang('table.accountDTO.bankName')},
   	            {"data":"curName",title:bt.lang('table.fbsBudgetProcess.curCode')},
        ],
        clickCallback:function(data,$tr,$elem,full){
        	$getAccBalance(data.account,data.bankAcc,data.curId);
            $elem.val(data.bankAcc)   //给当前输入框赋name值;
            $('#account').val(data.account);
            $('#curId').val(data.curId);
        },
        tableInitCallback:function(full,$elem){
        }
    });


    //-----------------------------按钮模块------------------------------------------
    //查询
    form.on('submit(doQuery)', function(data){
        //$loadCntrCheckDatas($getQueryParam("0"));
        //$loadBankCheckDatas($getQueryParam("1"));
        $(data.elem).removeClass("layui-btn-disabled");
        $("#checkFlag").val("1");
        $reloadTotalInfo();
        return false;
    });

    //对账
    form.on('submit(doCheck)', function(data){
    	if($doValidate()){
	    	var action = "/finacial/check/fccomparecenter/docheck";
	        $.post(basePath + action,data.field,function(result){
	        	if(result.success){
	        		layer.alert(bt.lang("finacial.check.succeed"), {icon:1});
	                $("#checkFlag").val("1");
	        		$reloadTotalInfo();
	            }else{
	            	layer.alert(bt.lang("finacial.check.fail"), {icon:2});
	            }
	        },'JSON');
    	}
		$(data.elem).removeClass("layui-btn-disabled");
    	return false;
    });

    //反对账
    form.on('submit(doReverseCheck)', function(data){
    	$doReverseCheck("5", "1");
		$(data.elem).removeClass("layui-btn-disabled");
    	return false;
    });

    //手工勾兑
    form.on('submit(doHandOver)', function(data){
        $doOverOp("1", "0");
		$(data.elem).removeClass("layui-btn-disabled");
        return false;
    });

    //手工反勾对
    form.on('submit(doReverseHandOver)', function(data){
    	$doOverOp("2", "2");
		$(data.elem).removeClass("layui-btn-disabled");
    	return false;
    });

    //确认未达
    form.on('submit(doHandStay)', function(data){
    	$doHandStay();
		$(data.elem).removeClass("layui-btn-disabled");
    	return false;
    });

    //结转
    form.on('submit(doAdjustOver)', function(data){
    	$doAdjustOver(data);
		$(data.elem).removeClass("layui-btn-disabled");
    	return false;
    });

    //打印
	form.on('submit(doPrint)', function(data){
		$doPrint(data);
		$(data.elem).removeClass("layui-btn-disabled");
		return false;
	});
	//未勾对记录
	form.on('radio(noHandRadios)', function(data){
    	$reloadTotalInfo();
		$(data.elem).removeClass("layui-btn-disabled");
    });
	//未达账记录
    form.on('radio(stayRadios)', function(data){
    	$reloadTotalInfo();
		$(data.elem).removeClass("layui-btn-disabled");
    });
    //已经勾对记录
    form.on('radio(handRadios)', function(data){
    	$reloadTotalInfo();
		$(data.elem).removeClass("layui-btn-disabled");
    });
    //已经对账记录
    form.on('radio(checkRadios)', function(data){
    	$reloadTotalInfo();
		$(data.elem).removeClass("layui-btn-disabled");
    });

	//子查询：企业日记账
	form.on('submit(searchCn)', function(data){
		if($doValidate()) {
			if(CntrCheckDatas == null || CntrCheckDatas == undefined){
				$loadCntrCheckDatas($getQueryParam("0"));
			}else{//重新渲染
				CntrCheckDatas.config.urldata = $getQueryParam("0");
				CntrCheckDatas.reload();
			}
		}
		$(data.elem).removeClass("layui-btn-disabled");
		return false;
	});
	//子查询：导出
	form.on('submit(importCn)', function(data){
		if($doValidate()) {
			var param = $getQueryParam("0");
		    var url = basePath + "/finacial/check/fccomparecenter/export/";
			openExportExcel(url,param)
		}
		$(data.elem).removeClass("layui-btn-disabled");
		return false;
	});
	//子查询：银行日记账
	form.on('submit(searchBank)', function(data){
		if($doValidate()) {
			if(BankCheckDatas == null || BankCheckDatas == undefined){
				$loadBankCheckDatas($getQueryParam("1"));
			}else{//重新渲染
				BankCheckDatas.config.urldata = $getQueryParam("1");
				BankCheckDatas.reload();
			}
		}
		$(data.elem).removeClass("layui-btn-disabled");
		return false;
	});
	//子查询：导出
	form.on('submit(importBank)', function(data){
		if($doValidate()) {
			var param = $getQueryParam("1");
			var url = basePath + "/finacial/check/fccomparecenter/export/";
			openExportExcel(url,param)
		}
		$(data.elem).removeClass("layui-btn-disabled");
		return false;
	});

	//-----------------------------方法模块------------------------------------------
	//重新加载列表数据
	$reloadTotalInfo = function(){
		if("1" == $("#checkFlag").val()){
			if(CntrCheckDatas == null || CntrCheckDatas == undefined){
				$loadCntrCheckDatas($getQueryParam("0"));
		    	$loadBankCheckDatas($getQueryParam("1"));
			}else{//重新渲染
				CntrCheckDatas.config.urldata = $getQueryParam("0");
				CntrCheckDatas.reload();
				BankCheckDatas.config.urldata = $getQueryParam("1");
				BankCheckDatas.reload();
			}
			$calAdjustBal();//计算余额调节表数据
		}
	}

	$getQueryParam = function(queryType){
		var queryParam = {};
		queryParam = { "queryType":queryType,"account" : $("#account").val(),
			"bankAcc" : $("#bankAcc")[0].value, "transDate" : dateTypes.get("transTime").val(),
            "checkFlag" : $("#checkFlag").val(), "curId" : $("#curId").val()}
		var noHandRadios = $('input[name="noHandRadios"]:checked').val();
		var stayRadios = $('input[name="stayRadios"]:checked').val();
		var handRadios = $('input[name="handRadios"]:checked').val();
		var checkRadios = $('input[name="checkRadios"]:checked').val();
		queryParam.noHandRadios = noHandRadios;
		queryParam.stayRadios = stayRadios;
		queryParam.handRadios = handRadios;
		queryParam.checkRadios = checkRadios;
		if("0" == queryType){	//按金额区间查询企业日记账
			queryParam.beginMoney = $("#beginMoneyCn").val();
			queryParam.endMoney = $("#endMoneyCn").val();
			queryParam.transDate = dateTypes.get("endTimeCn").val();
		}else{	//按金额区间查询银行日记账
			queryParam.beginMoney = $("#beginMoneyBank").val();
			queryParam.endMoney = $("#endMoneyBank").val();
			queryParam.transDate = dateTypes.get("endTimeBank").val();
		}
		return queryParam;
	}

	//加载企业日记账数据
	$loadCntrCheckDatas = function(queryParam){
		CntrCheckDatas = bt.bttable.init({
            elem:"#cntrTb",
            checkbox:true,
						headerCheckbox:true,
            url:basePath+"/finacial/check/fccomparecenter/toLoadCheckDatas",
            urldata:queryParam,
            mulselect:true,
            selectCss:false,
            scrollX:true,
            scrollY:"370px",
						formRenderInTable:true, // 默认为false，全页面绘制表单；true 代码只在表格内绘制表单
            columns:[
                {data:"tallyDate",className:'center',render:function(data,id){
						return bt.date.format(data.tallyDate,"yyyy-mm-dd");;
					}
				},
                {data:"billNo",render:function(data,id){
                	if(data.billNo==null){
                		return '';
                	}else{
                		return data.billNo;
                	}
                }},
                {data:"cdSign",className:'center',render:function(data,id){
                	if(data.cdSign=="1"){
                		return bt.lang("finacial.check.cdSign.debit");//借
                	}else{
                		return bt.lang("finacial.check.cdSign.credit");//贷
                	}
                }
                },
                {data:"money",className:'textRight',
                	render:function(data,id){
                		return bt.money.format(data.money);
                    }
                },
                {data:"voucherNo",
                	render:function(data,id){
                		if(data.voucherNo == null || data.voucherNo == undefined){
                			return "";
                		}else{
                			return data.voucherNo;
                		}
                    }
                }
            ],
            checkChildCallback:function(data,elem){ //更新勾选信息
                //data为本行数据; $tr 代表本行jq对象
            	//var c_rowdata=tabledata[elem.attr("row")];
            	$calCheckTotalData("cntrTb",data);
            },
            clickCallback:function(data,elem){//更新勾选信息
            	$calCheckTotalData("cntrTb",data);
            },
            tableInitCallback:function(data){
            	$calCheckTotalData("cntrTb",data);
            	$("#cntrTb tr").each(function(index,eml){//初始化表格背景色
            		if(index>0){
            			if(data[index-1].compareSign=="1"){//已对账
                			eml.style.backgroundColor="#4FBAE6";
                		}else if(data[index-1].compareSign=="2"){//手工勾兑
                			eml.style.backgroundColor="#B0DC81";
                		}else if(data[index-1].compareSign=="4"){//本期未达
                			eml.style.backgroundColor="#FF8080";
                		}else{//未对账
                			eml.style.backgroundColor="#FFFF80";
                		}
            		}
            	});
            },
            checkAllCallback:function(data){//全选
            	$calCheckTotalData("cntrTb",data);
            }
       });
	}


	//加载银行日记账数据
	$loadBankCheckDatas = function(queryParam){
		BankCheckDatas = bt.bttable.init({
            elem:"#bankTb",
            checkbox:true,
						headerCheckbox:true,
            url:basePath+"/finacial/check/fccomparecenter/toLoadCheckDatas",
            urldata:queryParam,
            mulselect:true,
            selectCss:false,
            scrollX:true,
            scrollY:"370px",
						formRenderInTable:true, // 默认为false，全页面绘制表单；true 代码只在表格内绘制表单
            columns:[
				{data:"tallyDate",className:'center',render:function(data,id){
						return bt.date.format(data.tallyDate,"yyyy-mm-dd");;
					}
				},
                {data:"billNo",render:function(data,id){
                	if(data.billNo==null){
                		return '';
                	}else{
                		return data.billNo;
                	}
                }},
                {data:"cdSign",className:'center',render:function(data,id){
                	if(data.cdSign=="1"){
                		return bt.lang("finacial.check.cdSign.debit");
                	}else{
                		return bt.lang("finacial.check.cdSign.credit");
                	}
                }},
                {data:"money",className:'textRight',
                	render:function(data,id){
                		return bt.money.format(data.money);
                    }
                },
                {data:"voucherNo",
                	render:function(data,id){
                		if(data.voucherNo == null || data.voucherNo == undefined){
                			return "";
                		}else{
                			return data.voucherNo;
                		}
                    }
                }
            ],
            checkChildCallback:function(data,elem){//更新勾选信息
        	   $calCheckTotalData("bankTb",data);
            },
            clickCallback:function(data){//更新勾选信息
            	$calCheckTotalData("bankTb",data);
            },
            tableInitCallback:function(data){//初始化表格背景色
            	$calCheckTotalData("bankTb",data);
            	$("#bankTb tr").each(function(index,eml){
            		if(index>0){
            			if(data[index-1].compareSign=="1"){//已对账
                			eml.style.backgroundColor="#4FBAE6";
                		}else if(data[index-1].compareSign=="2"){//手工勾兑
                			eml.style.backgroundColor="#B0DC81";
                		}else if(data[index-1].compareSign=="4"){//本期未达
                			eml.style.backgroundColor="#FF8080";
                		}else{//未对账
                			eml.style.backgroundColor="#FFFF80";
                		}
            		}
            	});
            },
            checkAllCallback:function(data,elem){//全选
            	 $calCheckTotalData("bankTb",data);
            }
       })
	}

    dateChangeCallback = function () { //
        $getAccBalance($("#account").val(), $("#bankAcc").val(),$('#curId').val());
    }

	//查询账号余额
	$getAccBalance = function(account,bankAcc,curId){
		$clearDatas();//清空数据
		var transDate = dateTypes.get("transTime").val();
		if(!transDate||transDate.trim()==""){
			layer.alert(bt.lang("finacial.check.selectEndDate"), {icon: 5});//选择结束日期
			return;
		}
		var paramType = $('#paramType').val();
		var accDisplayType = $("#accDisplayType").val();
		$.ajax({
			url:basePath+"/finacial/check/fccomparecenter?getAccBalance",
			method:'POST',
			data:{
				"account":account,
				"bankAcc":bankAcc,
				"transDate":transDate,
				"paramType":paramType,
				"accDisplayType":accDisplayType,
				"curId":curId
				},
			dataType:'json',
			success:function(data){
				if(data.success&&data.obj){
					$('#blnBalance').val(data.obj.blnBalance);
	                $('#bankBalance').val(data.obj.bankBalance);
	                $('#blnBalanceShow').html("<strong>"+bt.money.format(data.obj.blnBalance)+"</strong>");
	                $('#bankBalanceShow').html("<strong>"+bt.money.format(data.obj.bankBalance)+"</strong>");
	                $('#beginDate').html(data.obj.beginDate);
	                $('#checkDate').val(data.obj.lastOverDate);
	                dateTypes.get("transTime").attr("data-min",data.obj.beginDate);
                    dateTypes.get("transTime").val(transDate);
        			dateTypes.get("transTime").render();

					dateTypes.get("endTimeCn").attr("data-min",data.obj.beginDate);
					dateTypes.get("endTimeCn").val(transDate);
					dateTypes.get("endTimeCn").render();

					dateTypes.get("endTimeBank").attr("data-min",data.obj.beginDate);
					dateTypes.get("endTimeBank").val(transDate);
					dateTypes.get("endTimeBank").render();
				}else{
					layer.alert(bt.lang(data.messageEnum.toLowerCase().replaceAll("_","."),data.params), {icon:5});
					return false;
				}
			}
		});
	}

	//计算余额调节表数据
	$calAdjustBal = function(){
		var queryParam = {};
		queryParam = {"account" : $("#account").val(), "checkDate" : $("#checkDate").val(),
			"bankAcc" : $("#bankAcc")[0].value, "transDate" : dateTypes.get("transTime").val(),
			"paramType" : $('#paramType').val(),
			"blnBalance" : $("#blnBalance").val(), "bankBalance" : $("#bankBalance").val()};
		$.ajax({
			url:basePath+"/finacial/check/fccomparecenter?toLoadAdjustBalance",
			method:'POST',
			data:queryParam,
			dataType:'json',
			success:function(data){
				//显示调节后余额
				$("#blnAdjustBalance").val(bt.money.toNumber(data.obj.blnAdjustBalance));
				$("#bankAdjustBalance").val(bt.money.toNumber(data.obj.bankAdjustBalance));
				$("#adjustCntrMoney").html("<strong>"+ bt.money.format(data.obj.blnAdjustBalance)+"</strong>");
				$("#adjustBankMoney").html("<strong>"+ bt.money.format(data.obj.bankAdjustBalance)+"</strong>");
			}
		});
	}

    //勾兑操作
    $doReverseCheck = function(type, compareFlag){
        //反对账:5
        if(cntrCount == 0 && bankCount == 0){//同一栏位勾兑
            if ("5" == type){//反对账
                layer.alert(bt.lang("finacial.check.selectNumReCheckOp"),{icon:5});//请至少选择两条记录进行反对账！
                return;
            }
        }
        if("5" == type && (cntrCount == 0 || bankCount == 0)){//同一栏位勾兑
            layer.alert(bt.lang("finacial.check.selectBothReCheckOp"),{icon:5});//请选择两边对账记录进行反对账！
            return;
        }
        if(!$checkSelectData("cntrTb", type, compareFlag) || !$checkSelectData("bankTb", type, compareFlag)){
            return;
        }

        var cntrDatas = $getSelectData("cntrTb", type, compareFlag);//中心对账选择数据
        var bankDatas = $getSelectData("bankTb", type, compareFlag);//银行对账选择数据
        if(cntrCount == 0 || bankCount == 0) {//单边勾对
            layer.alert(bt.lang("finacial.check.selectBothReCheckOp"),{icon:5});//请选择两边对账记录进行反对账！
            return;
        }

        var cntrDMoney = cntrDatas["debitMoney"];
        var cntrCMoney = cntrDatas["creditMoney"];
        var bankDMoney = bankDatas["debitMoney"];
        var bankCMoney = bankDatas["creditMoney"];
        var cntrDMoneyLen = cntrDMoney.length;
        var cntrCMoneyLen = cntrCMoney.length;
        var bankDMoneyLen = bankDMoney.length;
        var bankCMoneyLen = bankCMoney.length;

        for(var i = 0; i < cntrDMoneyLen; i++) {
        	var debitMon = bt.money.toNumber(cntrDMoney[i]);
        	var equalFlag = false;
        	for(var j = 0; j < bankCMoneyLen; j++){
        		var creditMon = bt.money.toNumber(bankCMoney[j]);
        		if(debitMon == creditMon){
        			equalFlag = true;
                    bankCMoney[j] = "";
        			break;
        		}
        	}
        	if(equalFlag == false){
                //单笔勾对金额[{0}]，没有匹配的相同金额，不能进行勾对，请重新选择勾对数据！
                layer.alert(bt.lang("finacial.check.message.single.sameMoney.cntNotCheckOp",new Array(bt.money.format(debitMon))), {icon:5});
                return;
        	}
        }

        for(var i = 0; i < cntrCMoneyLen; i++) {
            var creditMon = bt.money.toNumber(cntrCMoney[i]);
            var equalFlag = false;
            for(var j = 0; j < bankDMoneyLen; j++){
                var debitMon = bt.money.toNumber(bankDMoney[j]);
                if(debitMon == creditMon){
                    equalFlag = true;
                    bankDMoney[j] = "";
                    break;
                }
            }
            if(equalFlag == false){
                //中心对账单反对账金额[{0}]没有匹配的相同金额，不能进行反对账，请重新选择数据！
                layer.alert(bt.lang("finacial.check.message.single.sameMoney.cntNotCheckOp",new Array(bt.money.format(creditMon))), {icon:5});
                return;
            }
        }

        for(var j = 0; j < bankCMoneyLen; j++){
            var creditMon = bankCMoney[j];
            if("" != creditMon) {
                creditMon = bt.money.toNumber(creditMon);
                //银行对账单反对账金额[{0}]没有匹配的相同金额，不能进行反对账，请重新选择数据！
                layer.alert(bt.lang("finacial.check.message.single.sameMoney.bankNotCheckOp",new Array(bt.money.format(creditMon))), {icon:5});
                return;
            }
        }
        for(var j = 0; j < bankDMoneyLen; j++) {
            var debitMon = bankDMoney[j];
            if("" != debitMon) {
                debitMon = bt.money.toNumber(debitMon);
                //银行对账单反对账金额[{0}]没有匹配的相同金额，不能进行反对账，请重新选择数据！
                layer.alert(bt.lang("finacial.check.message.single.sameMoney.bankNotCheckOp",new Array(bt.money.format(creditMon))), {icon:5});
                return;
            }
        }

        var queryParam = {};
        queryParam = {"account" : $("#account").val(), "checkDate" : $("#checkDate").val(),
            "bankAcc" : $("#bankAcc")[0].value, "transDate" : dateTypes.get("transTime").val(),
            "paramType" : $('#paramType').val(),"opType":type,
            "ctrPk" : cntrDatas["dataPk"], "bankPk" : bankDatas["dataPk"]};
        $.ajax({
            url:basePath+"/finacial/check/fccomparecenter?doHandOver",
            method:'POST',
            data:queryParam,
            dataType:'json',
            success:function(data){
                if(data.success){
                    if("5" == type){//反对账
                        layer.alert(bt.lang("finacial.recheck.succeed"), {icon:1});//反对账成功
                    }
                    $reloadTotalInfo();
                }
            }
        });
    }

    //勾兑操作
    $doOverOp = function(type, compareFlag){
    	//勾兑：1 0
    	//反勾兑：2 2
    	//未达:3
    	//反对账:5
    	//TODO(对账) 勾兑操作
    	if(cntrCount == 0 && bankCount == 0){//同一栏位勾兑
			if("1" == type){//勾对
				layer.alert(bt.lang("finacial.check.selectNumOp"),{icon:5});//请至少选择两条记录进行勾对！
				return;
			} else if ("2" == type){
				layer.alert(bt.lang("finacial.check.selectNumReverseOp"),{icon:5});//请至少选择两条记录进行反勾对！
				return;
			}
		}
    	if(!$checkSelectData("cntrTb", type, compareFlag) || !$checkSelectData("bankTb", type, compareFlag)){
			return;
		}

    	var cntrDatas = $getSelectData("cntrTb", type, compareFlag);//中心对账选择数据
		var bankDatas = $getSelectData("bankTb", type, compareFlag);//银行对账选择数据

		if(cntrCount == 0 || bankCount == 0){//单边勾对
			var checkDatas = {};
			if(cntrCount == 0){
				checkDatas = bankDatas;
			} else {
				checkDatas = cntrDatas;
			}
			var debitMoney = checkDatas["debitMoney"];//借方
			var creditMoney = checkDatas["creditMoney"];//贷方

			var debitLen = debitMoney.length;
			var creditLen = creditMoney.length;

			if(debitLen == 0 || creditLen == 0){//单边同借(贷)单笔勾兑
				var moneyLen = debitLen + creditLen;
				var overMoney = [];
				if(debitLen == 0){
					overMoney = creditMoney;
				} else {
					overMoney = debitMoney;
				}
				for(var i = 0; i < moneyLen; i++){
					if(overMoney[i] == ""){
						continue;
					}
					var debitMon = bt.money.toNumber(overMoney[i]);
					if(debitMon == 0){
						continue;
					}
					var equalFlag = false;
					for(var j = 0; j < moneyLen; j++){
						if(i == j){
							continue;
						}
						var creditMon = bt.money.toNumber(overMoney[j]);
						if(bt.money.add(debitMon ,creditMon) == 0){
							equalFlag = true;
							overMoney[j] = "";
							overMoney[i] = "";
							break;
						}
					}
					if(equalFlag == false){
						if("1" == type){//勾对
							//单笔勾对金额[{0}]，没有匹配的相同金额，不能进行勾对，请重新选择勾对数据！
							layer.alert(bt.lang("finacial.check.message.single.sameMoney.notOp",new Array(bt.money.format(debitMon))), {icon:5});
							return;
						} else if ("2" == type){
							//单笔反勾对金额[{0}]，没有匹配的相同金额，不能进行反勾对，请重新选择反勾对数据！
							layer.alert(bt.lang("finacial.check.message.single.sameMoney.notReverseOp",new Array(bt.money.format(debitMon))),{icon:5});
							return;
						}
					}
				}
			} else {//单边借贷单笔勾兑
				// if(debitLen != creditLen){
				// 	if("1" == type){//勾对
				// 		//单边勾对笔数不相等，不能进行勾对，请重新选择勾对数据！
				// 		layer.alert(bt.lang("finacial.check.message.single.countNotEq.notOp"), {icon:5});
				// 		return;
				// 	} else if ("2" == type){
				// 		//单边反勾对笔数不相等，不能进行反勾对，请重新选择反勾对数据！
				// 		layer.alert(bt.lang("finacial.check.message.single.countNotEq.notReverseOp"), {icon:5});
				// 		return;
				// 	}
				// }
				// for(var i = 0; i < debitLen; i++){
				// 	var debitMon = bt.money.toNumber(debitMoney[i]);
				// 	var equalFlag = false;
				// 	for(var j = 0; j < debitLen; j++){
				// 		var creditMon = bt.money.toNumber(creditMoney[j]);
				// 		if(debitMon == creditMon){
				// 			equalFlag = true;
				// 			creditMoney[j] = "";
				// 			break;
				// 		}
				// 	}
				// 	if(equalFlag == false){
				// 		if("1" == type){//勾对
				// 			//单笔勾对金额[{0}]，没有匹配的相同金额，不能进行勾对，请重新选择勾对数据！
				// 			layer.alert(bt.lang("finacial.check.message.single.sameMoney.notOp",new Array(bt.money.format(debitMon))), {icon:5});
				// 			return;
				// 		} else if ("2" == type){
				// 			//单笔反勾对金额[{0}]，没有匹配的相同金额，不能进行反勾对，请重新选择反勾对数据！
				// 			layer.alert(bt.lang("finacial.check.message.single.sameMoney.notReverseOp",new Array(bt.money.format(debitMon))),{icon:5});
				// 			return;
				// 		}
				// 	}
				// }
			}
		}else{//左栏和右栏勾兑
			var cntrDMoney = cntrDatas["debitMoney"];
			var cntrCMoney = cntrDatas["creditMoney"];
			var bankDMoney = bankDatas["debitMoney"];
			var bankCMoney = bankDatas["creditMoney"];
			var cntrDMoneyLen = cntrDMoney.length;
			var cntrCMoneyLen = cntrCMoney.length;
			var bankDMoneyLen = bankDMoney.length;
			var bankCMoneyLen = bankCMoney.length;
			var cntrDebitMoneySum = bt.money.toNumber(cntrDatas["debitMoneySum"]);
			var cntrCreditMoneySum = bt.money.toNumber(cntrDatas["creditMoneySum"]);
			var bankDebitMoneySum = bt.money.toNumber(bankDatas["debitMoneySum"]);
			var bankCreditMoneySum = bt.money.toNumber(bankDatas["creditMoneySum"]);

			if((cntrDMoneyLen == 0 && bankDMoneyLen == 0 && cntrCMoneyLen > 0 && bankCMoneyLen >0)//左借 = -右借/左贷 = -右贷(方向相同,正负相反)
					|| (cntrDMoneyLen > 0 && bankDMoneyLen > 0 && cntrCMoneyLen == 0 && bankCMoneyLen == 0)){
				if(bt.money.add(cntrDebitMoneySum, bankDebitMoneySum) != 0 || bt.money.add(cntrCreditMoneySum, bankCreditMoneySum) != 0){
					if("1" == type){//勾对
						//两边勾对金额和不为零，不能进行勾对，请重新选择勾对数据！
						layer.alert(bt.lang("finacial.check.message.much.sumNotZero.notOp"), {icon:5});
						return;
					} else if ("2" == type){
						//两边反勾对金额和不为零，不能进行反勾对，请重新选择反勾对数据！
						layer.alert(bt.lang("finacial.check.message.much.sumNotZero.notReverseOp"), {icon:5});
						return;
					}
				}
			}else if((cntrDMoneyLen==0&&cntrCMoneyLen>0&&bankDMoneyLen>0&&bankCMoneyLen>0)
					|| (cntrCMoneyLen==0&&cntrDMoneyLen>0&&bankDMoneyLen>0&&bankCMoneyLen>0)){
			   if((cntrDMoneyLen>0&&bt.money.sub(cntrDebitMoneySum, bt.money.sub(bankCreditMoneySum,bankDebitMoneySum))!=0)
					   ||(cntrCMoneyLen>0&&bt.money.sub(cntrCreditMoneySum,bt.money.sub(bankDebitMoneySum,bankCreditMoneySum))!=0)){
				   if("1" == type){
					   //两边勾对不满足对账公式(左借＝右贷－右借、左贷＝右借－右贷)
					   layer.alert(bt.lang("finacial.check.message.much.notMathMore.notOp"), {icon:5});
					   return;
				   }else if("2"==type){
					   //两边反勾对不满足对账公式(左借＝右贷－右借、左贷＝右借－右贷)
					   layer.alert(bt.lang("finacial.check.message.much.notMathMore.notReverseOp"), {icon:5});
					   return;
				   }
				}
			}else if(cntrDMoneyLen>0&&cntrCMoneyLen>0){
					if(bt.money.sub(bt.money.sub(cntrDebitMoneySum,cntrCreditMoneySum),bt.money.sub(bankCreditMoneySum,bankDebitMoneySum))!=0){
						if("1"==type){
							//两边勾对不满足对账公式(左借－左贷＝右贷－右借)
							layer.alert(bt.lang("finacial.check.message.much.notMath.notOp"), {icon:5});
							return;
						}else if("2"==type){
							//两边反勾对不满足对账公式(左借－左贷＝右贷－右借)
							layer.alert(bt.lang("finacial.check.message.much.notMath.notReverseOp"), {icon:5});
							return;
						}
				}
			} else {//左借 = 右贷/左贷 = 右借(方向相反,正负相同)
				if(bt.money.sub(cntrDebitMoneySum, bankCreditMoneySum) != 0 || bt.money.sub(cntrCreditMoneySum, bankDebitMoneySum) != 0){
					if("1" == type){//勾对
						//两边勾对金额不相等，不能进行勾对，请重新选择勾对数据！
						layer.alert(bt.lang("finacial.check.message.much.moneyNotEq.notOp"), {icon:5});
						return;
					} else if ("2" == type){
						//两边反勾对金额不相等，不能进行反勾对，请重新选择反勾对数据！
						layer.alert(bt.lang("finacial.check.message.much.moneyNotEq.notReverseOp"), {icon:5});
						return;
					}
				}
			}
		}

		var queryParam = {};
		queryParam = {"account" : $("#account").val(), "checkDate" : $("#checkDate").val(),
			"bankAcc" : $("#bankAcc")[0].value, "transDate" : dateTypes.get("transTime").val(),
			"paramType" : $('#paramType').val(),"opType":type,
			"ctrPk" : cntrDatas["dataPk"], "bankPk" : bankDatas["dataPk"]};
		$.ajax({
			url:basePath+"/finacial/check/fccomparecenter?doHandOver",
			method:'POST',
			data:queryParam,
			dataType:'json',
			success:function(data){
				if(data.success){
					if("1" == type){//勾对
						layer.alert(bt.lang("finacial.check.op.succeed"), {icon:1});//勾兑成功
					} else if("2" == type){
						layer.alert(bt.lang("finacial.check.ReverseOp.succeed"), {icon:1});//反勾兑成功
					}
					$reloadTotalInfo();
				}
			}
		});
    }

    //确认未达操作
    $doHandStay = function(){
    	if(cntrCount == 0 && bankCount == 0){
    		layer.alert(bt.lang("finacial.check.selectNumNotArrive"),{icon:5});//请至少选择一条记录进行未达！
    		return;
    	}
    	var type = "3";
		var compareFlag = [];
		compareFlag.push("0");
		compareFlag.push("4");
		if(!$checkSelectData("cntrTb", type, compareFlag) || !$checkSelectData("bankTb", type, compareFlag)){
			return;
		}

		var compareSign_cntr = $getCompareSign("cntrTb");
		var compareSign_bank = $getCompareSign("bankTb");
		if(compareSign_cntr != "" && compareSign_bank != "" && compareSign_cntr != compareSign_bank){
			layer.alert(bt.lang("finacial.check.notArriveNotSelectNotArrive"),{icon:5});//未达不能同时选择未勾对记录进行确认未达或者选择未达记录进行还原！
			return;
		}
		var cntrDatas = $getSelectData("cntrTb", type, compareSign_cntr);//中心对账选择数据
		var bankDatas = $getSelectData("bankTb", type, compareSign_bank);//银行对账选择数据
		if(compareSign_cntr == "4" || compareSign_bank == "4"){//已经确认未达
			type = "4";
		}

		var queryParam = {};
		queryParam = {"account" : $("#account").val(), "checkDate" : $("#checkDate").val(),
			"bankAcc" : $("#bankAcc")[0].value, "transDate" : dateTypes.get("transTime").val(),
			"paramType" : $('#paramType').val(),"opType":type,
			"ctrPk" : cntrDatas["dataPk"], "bankPk" : bankDatas["dataPk"]};
		$.ajax({
			url:basePath+"/finacial/check/fccomparecenter?doHandOver",
			method:'POST',
			data:queryParam,
			dataType:'json',
			success:function(data){
				if(data.success){
					//确认未达成功!
					layer.alert(bt.lang("finacial.check.notArrive.succeed"), {icon:1});
					$reloadTotalInfo();
				}else{
					//确认未达失败!
					layer.alert(bt.lang("finacial.check.notArrive.faild"), {icon:5});
				}
			}
		});

    }

    //获取对账类型
    $getCompareSign = function(tbId){
    	if(tbId=="cntrTb"&&cntrTbData.length>0){
    		return cntrTbData[0].compareSign;
    	}
    	if(tbId=="bankTb"&&bankTbData.length>0){
    		return bankTbData[0].compareSign;
    	}
    	return '';
    }

    //获取选中数据
    $getSelectData = function(tbId, type, compareFlag){
    	var datas;
    	if(tbId=="cntrTb"){datas = cntrTbData;}
    	if(tbId=="bankTb"){datas = bankTbData;}
		var length = datas.length;
		var flag = false;
		var moneys = {};
		var debitMoney = [];
		var creditMoney = [];
		var debitMoneySum = 0;
		var creditMoneySum = 0;
		var dataPk = "";
		if("1" == type || "2" == type || "3" == type || "5" == type){//勾对
			for(var i = 0; i < length; i++){
				var compareSign = datas[i].compareSign;
				if(compareSign == null || compareSign == "" || compareSign == undefined){
					compareSign = 0;//为空也为未对账
				}
				var cdSign = datas[i].cdSign;
				var money = bt.money.toNumber(datas[i].money);
				if(compareFlag == compareSign){
					if(cdSign == "1"){
						debitMoney.push(money);
						debitMoneySum = bt.money.add(debitMoneySum, money);
					} else {
						creditMoney.push(money);
						creditMoneySum = bt.money.add(creditMoneySum, money);
					}
					dataPk += datas[i].id+",";
				}
			}
		}
		moneys["debitMoney"] = debitMoney;
		moneys["creditMoney"] = creditMoney;
		moneys["debitMoneySum"] = debitMoneySum;
		moneys["creditMoneySum"] = creditMoneySum;
		moneys["dataPk"] = dataPk;
		return moneys;
    }

    //校验选中数据
    $checkSelectData = function(tbId, type, compareFlag){
    	var datas;
    	if(tbId=="cntrTb"){datas = cntrTbData;}
    	if(tbId=="bankTb"){datas = bankTbData;}

    	var length = datas.length;
		var flag = false;
		var compareSignNo = false;
		var compareSignStay = false;
		for(var i = 0; i < length; i++){
			var compareSign = datas[i].compareSign;
			if("3" == type){//未达
				if(compareFlag[0] != compareSign && compareFlag[1] != compareSign){
					flag = true;
					break;
				}
				if(compareFlag[0] == compareSign){//确认未达
					compareSignNo = true;
				}
				if(compareFlag[1] == compareSign){//还原
					compareSignStay = true;
				}
			} else {
				if(compareSign == null || compareSign == "" || compareSign == undefined){
					compareSign = 0;//为空也为未对账
				}
				if(compareFlag != compareSign){
					flag = true;
					break;
				}
			}
		}
		if(flag){
			if("1" == type){//勾对
				//勾对只能选择未勾对记录进行勾对！
				layer.alert(bt.lang("finacial.check.message.opNotOp"), {icon:5});
				return false;
			} else if ("2" == type){
				//反勾对只能选择已勾对记录进行反勾对！
				layer.alert(bt.lang("finacial.check.message.reverseOpSelectOp"), {icon:5});
				return false;
			} else if ("3" == type){
				//未达只能选择未勾对记录进行确认未达或者选择未达记录进行还原！
				layer.alert(bt.lang("finacial.check.message.notArriveSelectOpOrNotArrive"), {icon:5});
				return false;
            } else if ("5" == type){
                //反对账只能选择已对账记录进行反对账！
                layer.alert(bt.lang("finacial.check.message.checkedSelectOp"), {icon:5});
                return false;
			}
		} else {
			if ("3" == type && compareSignNo && compareSignStay){
				//未达不能同时选择未勾对记录进行确认未达或者选择未达记录进行还原！
				layer.alert(bt.lang("finacial.check.message.notArriveNotSelectOp"), {icon:5});
				return false;
			}
		}
		return true;
    }

    //结转操作
    $doAdjustOver = function(datas){
    	if($overCheck(bt.lang("finacial.check.Op.Adjust"))){//结转
			return;
		}
    	$.ajax({
			url:basePath+"/finacial/check/fccomparecenter?doAdjustOverOther",
			method:'POST',
			data:datas.field,
			dataType:'json',
			success:function(data){
				if(data.success){
					//"结转成功!
					layer.alert(bt.lang("finacial.check.Adjust.succeed"), {icon:1});
					$("#adjustId").val(data.obj);
					$clearDatas();
				}else{
					//结转失败!
					layer.alert(bt.lang("finacial.check.Adjust.faild"), {icon:5});
				}
			}
		});
    }

    //打印
    $doPrint = function(datas){
    	if(bt.empty($("#adjustId").val())){
			layer.alert(bt.lang("finacial.check.adjusted.print"),{icon:7});
			return false;
		}else{
			parent.layer.open({
		        type: 2,
		        area: ['75%', '80%'],
		        maxmin: true,
		        content: basePath + "/finacial/check/fcbankbalanceadjust/viewCntrBankDtls/" + $("#adjustId").val() + "?param=1"
		    });
		}
    }

    //结转判断
    $overCheck = function(title){
    	if($("#adjustId").val() != ""){
			return false;
		}
    	var checkFlag = $("#checkFlag").val();
		if(checkFlag == "0"){
			layer.alert(bt.lang("finacial.check.message.notCheck"), {icon:5});//未对账,请先对账！
			return true;
		}
		var blnAdjustBalance = $("#blnAdjustBalance").val();
		var bankAdjustBalance = $("#bankAdjustBalance").val();
		if(blnAdjustBalance && bankAdjustBalance){
			blnBalance = bt.money.toNumber(blnAdjustBalance);
			bankBalance = bt.money.toNumber(bankAdjustBalance);
			if(bt.money.add(blnBalance, 0 - bankBalance) - 0 != 0){
				//if(title.indexOf("导出")>=0)return false;
				//调节后余额不平，不能+title
				layer.alert(bt.lang("finacial.check.Op.AdjustBalanceNotEq",new Array(title)), {icon:5});
				return true;
			}
		}
		return false;
    }

    //清空数据
    $clearDatas = function(){
    	$("#blnBalance").val('');
    	$("#bankBalance").val('');
    	$('#blnBalanceShow').html("");
        $('#bankBalanceShow').html("");
        $("#blnAdjustBalance").val('');
		$("#bankAdjustBalance").val('');
		$("#adjustCntrMoney").html("");
		$("#adjustBankMoney").html("");
    	$("#checkFlag").val("0");
    	//清空列表数据
    	if(CntrCheckDatas != null && CntrCheckDatas != undefined){
    		CntrCheckDatas.config.urldata = {};
    		CntrCheckDatas.reload();
    		BankCheckDatas.config.urldata = {};
    		BankCheckDatas.reload();
    	}
    }

    //初始化表格信息
    $initTbData = function(tbId){
    	if("cntrTb"==tbId){
    		cntrCount = 0;
    		cntrDebMoney = 0;
    		cntrCreMoney = 0;
    	}else{
    		bankCount = 0;
    		bankDebMoney = 0;
    		bankCreMoney = 0;
    	}
    }

    //更新选中汇总数据
    $calCheckTotalData = function(tdId,data){
    	var selectdata = new Array();
       	bt.each(data,function(i,v){
       		if(v) {
				if (v.select) {
					selectdata.push(v);
				}
			}
			});
    	$initTbData(tdId);
    	$calCheckData(tdId,selectdata);
    	if(tdId=="cntrTb"){cntrTbData = selectdata;}
    	if(tdId=="bankTb"){bankTbData = selectdata;}

    }

    //设置勾选信息
    $calCheckData = function(tbId, rowData){
    	if("cntrTb"==tbId){
			var checkedArray = [];
			var checkedIds = "";
			var bankDate;
			if(BankCheckDatas){
				bankDate = BankCheckDatas.getDate();
			}
			if(!bankDate){
			    bankDate = [];
            }
			var noMatching = [];
    		for(var i=0;i<rowData.length;i++) {
				//是否匹配到了银行日记账
				var isMatching = false;
				if(handOverFlag) {
                    if (rowData[i].cdSign == "1") {
                        cntrDebMoney = bt.money.add(cntrDebMoney, rowData[i].money);
                    } else {
                        cntrCreMoney = bt.money.add(cntrCreMoney, rowData[i].money);
                    }
                } else {
                    for (var j = 0; j < bankDate.length; j++) {
                        if (checkedIds.indexOf(bankDate[j].id) >= 0) {
                            //已勾选的跳过
                            continue;
                        }
                        if (rowData[i].cdSign == "1") {
                            if (bankDate[j].cdSign == "0" && rowData[i].money == bankDate[j].money) {
                                checkedArray.push({id: bankDate[j].id, checked: true})
                                checkedIds += bankDate[j].id + ',';
                                isMatching = true;
                                break
                            }
                        } else {
                            if (bankDate[j].cdSign == "1" && rowData[i].money == bankDate[j].money) {
                                checkedArray.push({id: bankDate[j].id, checked: true})
                                checkedIds += bankDate[j].id + ',';
                                isMatching = true;
                                break
                            }
                        }
                    }
                    if (!isMatching) {
                        noMatching.push({id: rowData[i].id, checked: false});
                        rowData.splice(i, 1);
                        i--;
                    } else {
                        if (rowData[i].cdSign == "1") {
                            cntrDebMoney = bt.money.add(cntrDebMoney, rowData[i].money);
                        } else {
                            cntrCreMoney = bt.money.add(cntrCreMoney, rowData[i].money);
                        }
                    }
                }
    		}
            if(!handOverFlag) {
                for(var j=0;j<bankDate.length;j++){
                    if(checkedIds.indexOf(bankDate[j].id) < 0) {
                        checkedArray.push({id: bankDate[j].id, checked: false})
                    }
                }
				if(BankCheckDatas){
					BankCheckDatas.setSelectedRow(checkedArray);
					$calCheckTotalData("bankTb",BankCheckDatas.getSelectedData());
				}
            }
    		cntrCount = rowData.length;
    		$("#cntrCount").html(rowData.length);
    		$("#cntrDebMoney").html(bt.money.format(cntrDebMoney, {scale: 2}));
    		$("#cntrCreMoney").html(bt.money.format(cntrCreMoney, {scale: 2}));
			CntrCheckDatas.setSelectedRow(noMatching);
    	}else{
    		for(var i=0;i<rowData.length;i++){
    			if(rowData[i].cdSign == "1"){
    				bankDebMoney = bt.money.add(bankDebMoney, rowData[i].money);
    			}else{
    				bankCreMoney = bt.money.add(bankCreMoney, rowData[i].money);
    			}
    		}
    		bankCount = rowData.length;
    		$("#bankCount").html(rowData.length);
    		$("#bankDebMoney").html(bt.money.format(bankDebMoney, {scale: 2}));
    		$("#bankCreMoney").html(bt.money.format(bankCreMoney, {scale: 2}));
    	}
    }

    //校验页面信息
    $doValidate = function(){
    	var transDate = dateTypes.get("transTime").val();
		if(!transDate||transDate.trim()==""){
			//请先选择结束日期！
			layer.alert(bt.lang("finacial.check.selectEndDate"), {icon: 2});
			return false;
		}
    	if($("#bankAcc")[0].value==""){
    		//请选择银行账号
    		layer.alert(bt.lang("finacial.check.selectBankAcc"), {icon:2});
    		return false;
    	}
    	if($("#blnBalance").val()==""||$("#bankBalance").val()==""){
    		//请先初始化账户余额
    		layer.alert(bt.lang("finacial.check.firstInitBal"), {icon:2});
    		return false;
    	}
    	return true;
    }
	dateChangeCallback();

    // 说明显示隐藏
    showDesc();
});

function openExportExcel(exportUrl, queryParam) {
	var form = $("<form></form>").attr("style", "display:none").attr("action", exportUrl).attr("method", "post");
	try {
		form.append($("<input></input>").attr("type", "hidden").attr("name", "queryType").attr("value", queryParam.queryType));
		form.append($("<input></input>").attr("type", "hidden").attr("name", "account").attr("value", queryParam.account));
		form.append($("<input></input>").attr("type", "hidden").attr("name", "bankAcc").attr("value", queryParam.bankAcc));
		form.append($("<input></input>").attr("type", "hidden").attr("name", "checkFlag").attr("value", queryParam.checkFlag));
		form.append($("<input></input>").attr("type", "hidden").attr("name", "curId").attr("value", queryParam.curId));
		form.append($("<input></input>").attr("type", "hidden").attr("name", "noHandRadios").attr("value", queryParam.noHandRadios));
		form.append($("<input></input>").attr("type", "hidden").attr("name", "stayRadios").attr("value", queryParam.stayRadios));
		form.append($("<input></input>").attr("type", "hidden").attr("name", "handRadios").attr("value", queryParam.handRadios));
		form.append($("<input></input>").attr("type", "hidden").attr("name", "checkRadios").attr("value", queryParam.checkRadios));
		form.append($("<input></input>").attr("type", "hidden").attr("name", "beginMoney").attr("value", queryParam.beginMoney));
		form.append($("<input></input>").attr("type", "hidden").attr("name", "endMoney").attr("value", queryParam.endMoney));
		form.append($("<input></input>").attr("type", "hidden").attr("name", "transDate").attr("value", queryParam.transDate));
		form.appendTo('body');
		form.submit();
	} catch (e) {
		console.log(e);
	} finally {
		form.remove();
	}
}

//表格是否选中
function getTableCheckStatusArray(data,allChecked) {
    var newData = []
    if(data && data.length){
        for (var i = 0; i < data.length; i++) {
            newData.push({
                id:data[i].id,
                checked:allChecked
            })
        }
    }
    return newData
}

//显示操作指引
function showDesc() {
    var tip_index1 = 0;
    $('#check_desc').mouseenter(function () {
        tip_index1 = layer.tips(bt.lang('fin.check.process.desc'), '#check_desc', {time: 30000});
    }).mouseleave(function () {
        layer.close(tip_index1);
    });

}
