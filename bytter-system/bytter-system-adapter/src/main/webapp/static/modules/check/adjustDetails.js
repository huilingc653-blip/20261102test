var bankRecTbSum = 0;//银行已收中心未收合计
var ctrRecTbSum = 0;//中心已收银行未收合计
var bankPayTbSum = 0;//银行已付中心未付合计
var ctrPayTbSum = 0;//中心已付银行未付合计
var loadtableNum = 0;//初始化第几个表格
var dataType;
bt.use(['form','date','bttable','moneyType'], function(){
	$ = bt.jquery,form = bt.form(),moneyFun = bt.moneyType.getFun();
	var b="380px";
	/**
	 * 加载数据
	 * 数据来源 dataType 1银行数据 2中心数据 3ERP数据
	 */
		if($("#checkType").val()==0){
			dataType =2;
		}else{
			dataType=3;
		}
		//银行已收中心未收	
		bt.bttable.init({
	        elem:"#bankRecTb",
	        url:basePath+"/finacial/check/fcbankbalanceadjust/queryAdjustDetaiData",
	        urldata:{"dataType":1,"queryType":4,"adjId":$("#adjId").val()},
	        scrollX:true,
	        
	        columns:[
	            {data:"tallyDate",className:'center',
	            	render:function(data,id){
	            		return bt.date.format(data.tallyDate, 'yyyy-mm-dd');
	                }
	            },
	            {data:"billNo",className:'center',
	            	render:function(data,id){
		            	if(data.billNo==null){
		            		return '';
		            	}else{
		            		return data.billNo;
		            	}
		            }
	            },
	            {data:"voucherNo",className:'center',
	            	render:function(data,id){
                		if(data.voucherNo == null || data.voucherNo == undefined){
                			return "";
                		}else{
                			return data.voucherNo;
                		}
                    }
	            },
	            {data:"money",className:'textRight',
	            	render:function(data,id){
	            		return moneyFun.format(data.money);
	                }	
	            },
	            {data:"checkRecord"}
	        ],
	        tableInitCallback:function(data){            
	            var moneySum = 0;
	            layui.each(data,function(i,d){
	            	moneySum += parseFloat(d.money);
	            })
	            $('td[name="bankRecTbSumShow"]').html("<strong>"+moneyFun.format(moneySum)+"</strong>");
	            bankRecTbSum = moneySum;
	            loadtableNum += 1;
	            calAdjustBalance(loadtableNum);
	        },
	   });
	
		//中心已收银行未收
		bt.bttable.init({
	        elem:"#ctrRecTb",
	        url:basePath+"/finacial/check/fcbankbalanceadjust/queryAdjustDetaiData",
	        urldata:{"dataType":dataType,"queryType":2,"adjId":$("#adjId").val()},
	        scrollX:true,
	       
	        columns:[
	            {data:"tallyDate",className:'center',
	            	render:function(data,id){
	            		return bt.date.format(data.tallyDate, 'yyyy-mm-dd');
	                }
	            },
	            {data:"billNo",className:'center',
	            	render:function(data,id){
		            	if(data.billNo==null){
		            		return '';
		            	}else{
		            		return data.billNo;
		            	}
		            }
	            },
	            {data:"voucherNo",className:'center',
	            	render:function(data,id){
                		if(data.voucherNo == null || data.voucherNo == undefined){
                			return "";
                		}else{
                			return data.voucherNo;
                		}
                    }	
	            },
	            {data:"money",className:'textRight',
	            	render:function(data,id){
	            		return moneyFun.format(data.money);
	                }	
	            },
	            {data:"checkRecord"}
	        ],
	        tableInitCallback:function(data){            
	            var moneySum = 0;
	            layui.each(data,function(i,d){
	            	moneySum += parseFloat(d.money);
	            })
	            $('td[name="ctrRecTbSumShow"]').html("<strong>"+moneyFun.format(moneySum)+"</strong>");
	            ctrRecTbSum = moneySum;
	            loadtableNum += 1;
	            calAdjustBalance(loadtableNum);
	        },
	   });
		
		//银行已付中心未付
		bt.bttable.init({
	        elem:"#bankPayTb",
	        url:basePath+"/finacial/check/fcbankbalanceadjust/queryAdjustDetaiData",
	        urldata:{"dataType":1,"queryType":3,"adjId":$("#adjId").val()},
	        scrollX:true,
	       
	        columns:[
	            {data:"tallyDate",className:'center',
	            	render:function(data,id){
	            		return bt.date.format(data.tallyDate, 'yyyy-mm-dd');
	                }
	            },
	            {data:"billNo",className:'center',
	            	render:function(data,id){
		            	if(data.billNo==null){
		            		return '';
		            	}else{
		            		return data.billNo;
		            	}
		            }
	            },
	            {data:"voucherNo",className:'center',
	            	render:function(data,id){
                		if(data.voucherNo == null || data.voucherNo == undefined){
                			return "";
                		}else{
                			return data.voucherNo;
                		}
                    }	
	            },
	            {data:"money",className:'textRight',
	            	render:function(data,id){
	            		return moneyFun.format(data.money);
	                }	
	            },
	            {data:"checkRecord"}
	        ],
	        tableInitCallback:function(data){            
	            var moneySum = 0;
	            layui.each(data,function(i,d){
	            	moneySum += parseFloat(d.money);
	            })
	            $('td[name="bankPayTbSumShow"]').html("<strong>"+moneyFun.format(moneySum)+"</strong>");
	            bankPayTbSum = moneySum;
	            loadtableNum += 1;
	            calAdjustBalance(loadtableNum);
	        },
	   });
		
		//中心已付银行未付
		bt.bttable.init({
	        elem:"#ctrPayTb",
	        url:basePath+"/finacial/check/fcbankbalanceadjust/queryAdjustDetaiData",
	        urldata:{"dataType":dataType,"queryType":1,"adjId":$("#adjId").val()},
	        scrollX:true,
	        
	        columns:[
	            {data:"tallyDate",className:'center',
	            	render:function(data,id){
	            		return bt.date.format(data.tallyDate, 'yyyy-mm-dd');
	                }
	            },
	            {data:"billNo",className:'center',
	            	render:function(data,id){
		            	if(data.billNo==null){
		            		return '';
		            	}else{
		            		return data.billNo;
		            	}
		            }
	            },
	            {data:"voucherNo",className:'center',
	            	render:function(data,id){
                		if(data.voucherNo == null || data.voucherNo == undefined){
                			return "";
                		}else{
                			return data.voucherNo;
                		}
                    }	
	            },
	            {data:"money",className:'textRight',
	            	render:function(data,id){
	            		return moneyFun.format(data.money);
	                }	
	            },
	            {data:"checkRecord"}
	        ],
	        tableInitCallback:function(data){            
	            var moneySum = 0;
	            layui.each(data,function(i,d){
	            	moneySum += parseFloat(d.money);
	            })
	            $('td[name="ctrPayTbSumShow"]').html("<strong>"+moneyFun.format(moneySum)+"</strong>");
	            ctrPayTbSum = moneySum;
	            loadtableNum += 1;
	            calAdjustBalance(loadtableNum);
	        },
	   });
		
		function calAdjustBalance(num){
			if(num == 4){//四个表格加载完成
//				$("#blnAdjustBalance").html("<strong>"+moneyFun.format(moneyFun.add(parseFloat($("#blnInitBalance").val()),moneyFun.sub(bankRecTbSum,bankPayTbSum)))+"</strong>");
//				$("#bankAdjustBalance").html("<strong>"+moneyFun.format(moneyFun.add(parseFloat($("#bankInitBalance").val()),moneyFun.sub(ctrRecTbSum,ctrPayTbSum)))+"</strong>");
			}
		}
		
		//关闭窗口
	    doClose = function(){
	    	parent.layer?parent.layer.closeAll():layer.closeAll();
	    }
	    
	 
		
});
function print() {
	var LODOP=getLodop();
	$(".no-print").hide();
	LODOP.SET_PRINT_MODE("PRINT_PAGE_PERCENT","Width:90%");
	LODOP.ADD_PRINT_HTM(0,0,"100%","100%",document.documentElement.innerHTML);
	$(".no-print").show();
	LODOP.PREVIEW();
}



