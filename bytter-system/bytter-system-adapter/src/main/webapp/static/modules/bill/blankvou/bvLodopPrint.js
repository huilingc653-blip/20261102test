var LODOP;
window.onload = function(){ 
	LODOP=getLodop(); 
} 

var initWidth=0.8;//"5.00cm";
var height="0.70cm";
var initfont=11;
var designSign=0;
var setPrintData=new Array();

/**
 * 凭证打印统一调用方法
 * @param printData 凭证套打数据打印格式信息
 * @param flag true为打印，false为打印预览
 * @param desigeFlag true为打印调整，false为否打印调整
 */
var billFormatPrint=function (printData,flag, desigeFlag){
	if(!desigeFlag && designSign == 1){
		if(flag){
			LODOP.PRINTA();
		} else {
			LODOP.PREVIEW();
		}
		designSign=0; 
		return ;
	}
	var isPrint=printBillFormat(printData, flag, desigeFlag);
}


/**
 * 凭证打印统一调用方法
 * @param data 凭证套打数据打印格式信息
 * @param flag true为打印，false为打印预览
 * @param desigeFlag true为打印调整，false为否打印调整
 */
function printBillFormat(data, flag, desigeFlag){
	if(!data||data.length==0||data.length==undefined){
		// layer.msg(bt.lang('sys.message.opdata.error'), {icon: 7});
		layer.msg(bt.lang(data.msg), {icon: 7});
		return false;
	}
	printFmtOp(data,0,flag,desigeFlag);
}

/**
 * 分不同模版打印数据
 * @param data
 * @param i
 * @param flag
 * @param desigeFlag
 */
function printFmtOp(data,i,flag,desigeFlag){
	setPrintData=new Array();
	var isNextPage=false; 
	var printId="";
	for (i;i < data.length;i++){
		var info=data[i];
		if(printId!=info.id){
			var posts=info.paperSize.split(",");
			LODOP.PRINT_INIT("凭证套打打印");
            var left =0;
            var top = 0;
            if(info.leftBorder){
                left=parseFloat(info.leftBorder);
            }
            if(info.upBorder){
                top=parseFloat(info.upBorder);
            }
			LODOP.PRINT_INITA(top,left,posts[0],posts[1],"凭证套打打印");
			LODOP.SET_SHOW_MODE("BKIMG_IN_PREVIEW",true);
			LODOP.SET_PRINT_TEXT_STYLE(1,"宋体",9,0,0,0,1);
			LODOP.SET_PRINT_STYLE("FontSize",11);
			if(desigeFlag){
                LODOP.SET_PRINT_MODE("PROGRAM_CONTENT_BYVAR",true);//生成程序时，内容参数有变量用变量，无变量用具体值
				// LODOP.SET_PRINT_MODE("POS_BASEON_PAPER", true)
			}
            if(data[i].backgroundImage){
                var allowOnceArr=getAllowOnce();
                var allowOnce="";
                if(allowOnceArr.length==1){
                    allowOnce=allowOnceArr[0];
                }
                var imgHtml="<img border='0' src='"+basePathAll+"/download/attachment?id="+data[i].backgroundImage+"&_allow_once_="+allowOnce+"&date="+(new Date().getTime())+"' />";
                LODOP.ADD_PRINT_SETUP_BKIMG(imgHtml);
            }
		}
	
		//左下边距
		var leftBorder =0;
        var upBorder = 0;

		if(isNextPage){
			LODOP.NewPageA();//分页
		}else{
			isNextPage=true;
		}
		setPrintInfos(info,leftBorder,upBorder,desigeFlag);
		setPrintData.push(data[i]);
		if(printId!=info.id||data.length==(i+1)){
			if(printId||data.length==(i+1)||info.id!=data[i+1].id){
				if(desigeFlag){
					designSign=1;
                    LODOP.SET_PREVIEW_WINDOW(1,0,0,800,600,"");
                    // LODOP.PRINT_SETUP();
                    LODOP.PRINT_DESIGN();
                    if (LODOP.CVERSION){
                        CLODOP.On_Return=function(TaskID,Value){
                            opPrintCoord(data,Value);
                        };
                        LODOP.PRINT_DESIGN();
					}else{
                        opPrintCoord(data,LODOP.PRINT_DESIGN());
					}
				}else{
					if(flag){
						if (LODOP.CVERSION){
							CLODOP.On_Return=function(TaskID,Value){
								//var statusOK=LODOP.GET_VALUE('PRINT_STATUS_OK',Value);
								if(Value){
									printOccupyVouNo(setPrintData);
								}else{
									layer.alert(bt.lang('bill.blankvou.printError'), {icon: 2});
									return;
								}
								if(data.length!=(i+1)){
									printFmtOp(data,i+1,flag,desigeFlag);
								}
							};
							LODOP.PRINTA();
						}else{
							var Value=LODOP.PRINTA();
							//var statusOK=LODOP.GET_VALUE('PRINT_STATUS_OK',Value);
							if(Value){
								printOccupyVouNo(setPrintData);
							}else{
								layer.alert(bt.lang('bill.blankvou.printError'), {icon: 2});
								return;
							}
							if(data.length!=(i+1)){
								printFmtOp(data,i+1,flag,desigeFlag);
							}
						}
					} else {
						if (LODOP.CVERSION){
							CLODOP.On_Return=function(TaskID,Value){
								//var statusOK=LODOP.GET_VALUE('PRINT_STATUS_OK',Value);
								if(Value>0){
									printOccupyVouNo(setPrintData);
								}else{
//									layer.alert('打印失败！', {icon: 2});
//									return;
								}
								if(data.length!=(i+1)){
									printFmtOp(data,i+1,flag,desigeFlag);
								}
							};
                            LODOP.SET_PREVIEW_WINDOW(1,0,0,800,600,"");
                            LODOP.PREVIEW()
						}else{
                            LODOP.SET_PREVIEW_WINDOW(1,0,0,800,600,"");
							var Value=LODOP.PREVIEW();
							//var statusOK=LODOP.GET_VALUE('PRINT_STATUS_OK',Value);
							if(Value>0){
								printOccupyVouNo(setPrintData);
							}else{
//								layer.alert('打印失败！', {icon: 2});
//								return;
							}
							if(data.length!=(i+1)){
								printFmtOp(data,i+1,flag,desigeFlag);
							}
						}
					}
				}
				return;
			}
			printId=info.id;
		}
	}
}


/**
 * 设置数据的打印坐标
 * @param data
 * @param leftBorder
 * @param upBorder
 */
function setPrintInfos(data,leftBorder,upBorder,desigeFlag){
	var dataInfo=data.printFmtDtl;
	for (var i = 0;i < dataInfo.length;i++){
		var fontSize=initfont;
		var fontSpace = 0;
		var fontBr = 0;
		if(dataInfo[i]&&dataInfo[i].otherVal!=null&&dataInfo[i].otherVal!=''&&dataInfo[i].otherVal!=undefined){
			if(dataInfo[i].fontSize){
				fontSize=dataInfo[i].fontSize;
			}
			if(dataInfo[i].fontSpace){
				fontSpace=dataInfo[i].fontSpace;
			}
			if(dataInfo[i].charSizeBr){
				fontBr=dataInfo[i].charSizeBr;
			}
			var pvalue =dataInfo[i].otherVal;
			if(dataInfo[i].prefixVal&&dataInfo[i].fontSpace&&!isNaN(pvalue)){
				pvalue=pvalue;
			}
		    if(dataInfo[i].prefixVal){
		    	pvalue=dataInfo[i].prefixVal+pvalue;
		    }
		    var printCoords=dataInfo[i].printCoord.split(",");
		    var leftPoint=parseFloat(printCoords[0]);
		    var upPoint=parseFloat(printCoords[1]);
		    var printType=dataInfo[i].valPrintType;
		    if(!fontBr&&printType!='07'){
				LODOP.SET_PRINT_STYLE("FontSize",fontSize);
		    }
			var width=initWidth+((fontSize-initfont)*0.2);
			if(fontSpace){
				var moneySpace = parseFloat(fontSpace);
				var upPointNew = upPoint - upBorder;
				var widthNew=width+"cm";
				var leftPointNew = leftPoint+leftBorder;
				for(var k = 0; k < pvalue.length; k++){
					var moneyNumStr = pvalue.substring(k, k+1);
					if(moneyNumStr==" "){
						continue;
					}
					var leftVal=(leftPointNew/100) +"cm";
					var upVal=(upPointNew/100)+"cm";
					LODOP.ADD_PRINT_TEXT(upVal,leftVal,widthNew,height,moneyNumStr);
                    if(desigeFlag&&k==0){
                        LODOP.SET_PRINT_STYLEA(0,"ContentVName","MyDataKey"+i);//设置内容参数的变量名
                    }
					LODOP.SET_PRINT_STYLEA(0,"Bold",1);
					leftPointNew=leftPointNew+moneySpace;
				}
			}else{
				if(fontBr){
					var num = parseFloat(fontBr);
					var pvlength = pvalue.length;
					var size = Math.floor(pvlength / num);
					var left = pvlength % num;
					if(left > 0){
						size += 1;
					}
					var tdHeight = 35+((fontSize-initfont)*3);
					fontSize=fontSize-(size-1);
					upPoint=upPoint-((tdHeight/2)*(size-1));
					LODOP.SET_PRINT_STYLE("FontSize",fontSize);
					var widthNew=(width*fontBr)+"cm";
					for(var k = 0; k < size; k++){
						var infoTemp = pvalue.substring(k * num, (k + 1)* num);
						var leftVal=(leftPoint/100 + leftBorder/100) +"cm";
						var upVal=( ( upPoint - upBorder + (tdHeight * k) ) / 100 ) +"cm";
						LODOP.ADD_PRINT_TEXT(upVal,leftVal,widthNew,height,infoTemp);
                        if(desigeFlag&&k==0){
                            LODOP.SET_PRINT_STYLEA(0,"ContentVName","MyDataKey"+i);//设置内容参数的变量名
                        }
						LODOP.SET_PRINT_STYLEA(0,"Bold",1);
					}
				}else{
					var widthNew=(width*pvalue.length)+"cm";
					var leftVal=(leftPoint/100 + leftBorder/100) +"cm";
					var upVal=((upPoint - upBorder) /100)+"cm";
					if(printType=='07'){
						var valNews;
						if(dataInfo[i].otherVal && dataInfo[i].otherVal!='null' && dataInfo[i].otherVal!='test'){
							valNews=dataInfo[i].otherVal.split(",");
							var imgId;
							var imgWidth=widthNew;
							var imgHeight=height;
							if(valNews[0]){
								imgId=valNews[0];
							}
							if(valNews[1]){
								imgWidth=valNews[1]+"px";
							}
							if(valNews[2]){
								imgHeight=valNews[2]+"px";
							}
							if(valNews){
								var allowOnceArr=getAllowOnce();
								var allowOnce="";
								if(allowOnceArr.length==1){
									allowOnce=allowOnceArr[0];
								}
								var imgHtml="<img id='imger' src='"+basePathAll+"/download/attachment?id="+imgId+"&_allow_once_="+allowOnce+"&date="+(new Date().getTime())+"' style='z-index: -1; position: absolute; width:'"+imgWidth+"';height:'"+imgHeight+"'/>";
								LODOP.ADD_PRINT_IMAGE(upVal,leftVal,imgWidth,imgHeight,imgHtml);
								LODOP.SET_PRINT_STYLEA(0,"TransColor","#FFFFFF");
							}
						}else if(dataInfo[i].otherVal=='test'){
							var allowOnceArr=getAllowOnce();
							var allowOnce="";
							if(allowOnceArr.length==1){
								allowOnce=allowOnceArr[0];
							}
							var imgHtml="<img id='imger' src='"+basePathAll+"/download/resource?code=402880455c5dcd90015c5dcdda790L03&_allow_once_="+allowOnce+"&date="+(new Date().getTime())+"' style='z-index: -1; position: absolute; width:'150px';height:'150px'/>";
							LODOP.ADD_PRINT_IMAGE(upVal,leftVal,'150px','150px',imgHtml);
                            if(desigeFlag){
                                LODOP.SET_PRINT_STYLEA(0,"ContentVName","MyDataKey"+i);//设置内容参数的变量名
                            }
							LODOP.SET_PRINT_STYLEA(0,"TransColor","#FFFFFF");
						}
					}else{
						LODOP.ADD_PRINT_TEXT(upVal,leftVal,widthNew,height,pvalue);
                        if(desigeFlag){
                            LODOP.SET_PRINT_STYLEA(0,"ContentVName","MyDataKey"+i);//设置内容参数的变量名
                        }
						LODOP.SET_PRINT_STYLEA(0,"Bold",1);
					}
				}
			}

		}
	}
}
var getAllowOnce=function(size){
	if(!size){
		size=1;
	}
	var allowOnceArr;
	bt.jquery.ajax({
    	type: "GET",
    	url: basePath + "/allowOnce?size="+size,
       	dataType:"json",
       	async: false, 
    	success: function(result){
    		if(result){
    			allowOnceArr = result;
    		}
    	}
    });
	return allowOnceArr;
}
