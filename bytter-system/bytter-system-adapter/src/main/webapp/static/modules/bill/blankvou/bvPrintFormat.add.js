var getDataInfo;
var fromObj;
bt.use(['form','layedit','moneyType','select','dicts','bttable','verify','element','upload'], function(){
    var $ = bt.jquery,form = bt.form();
    var dicts = bt.dicts.init();
    fromObj=form;
    form.verify({
    	fmtNoCheck : function(value) {
    		if(value){
    			var success=false;
    			$.ajax({ 
    				type: 'POST',
    				url: basePath+"/bill/blankvou/bvprintformat/checkFmtNo", 
    				data:{'opType':'add','fmtNo':value},	
    				async: false, 
    				dataType:'json',
    				success: function(result){
    		        	if(!result.success){
    		        		success=true;
    		        	}
    		      }});
    			if(success){
	        		return bt.lang('bill.message.code.unique');
	        	}
    		}
		},
		coordVfy : function(value) {
		    if(!/^\d+(,){1}\d+$/.test(value)){
		        return bt.lang('sys.form.verify.fmt.error',['***,***']);
		    }
		}

	});

    var upload = layui.upload;
    layui.upload({
        url: basePath+'/upload/attachment?code=bill'
        ,success: function(res, input){
            if(typeof res.success == 'boolean' && !res.success){
                layer.alert(bt.lang(res.msg));
            }else{
                $("#imger").attr("src", basePath+'/download/attachment?id='+res[0].id);
                $("#backgroundImage").val(res[0].id);
            }
        }
    });

    // 监听提交
    form.on('submit(submit)', function(data){
    	bt.loading.show();
        var action = "/bill/blankvou/bvprintformat/save";
        var dataInfo=getDataInfo();
        $.ajax({
        	type: "POST",
        	url: basePath + action,
           	data: dataInfo,//将对象序列化成JSON字符串
           	dataType:"json",
           	contentType : 'application/json;charset=utf-8', //设置请求头信息 
        	success: function(result){
        		bt.loading.hide();
                parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
        		if(result.success){
        			bt.navtab.closeCurrent();
                }
        	},
        	error: function(res){
        		bt.loading.hide();
        	}
        });

        return false;
    });
    
    bt.select.init({
        elem:'#voucherType',
        id:"vouTid",
        name:"vouTid",
        valueField:"id",
        textField:"name",
        search: true,
        required: true,
        disabled: false,
        url:basePath+"/bill/blankvou/bvvouchertype/queryVoucherTypeValid" ,
        change:function(data,text){
        	initSceneInfo();
        	initPrintMRTable();
        	initPrintFmtDtlTable();
        }
    });
    var rowMR=0;
    var rowFmtDtl=0;

    var initSceneInfo=function(){
    	var voucherTypeId=$("#voucherType").find('select').val();
    	bt.select.init({
            elem:'#voucherTypeScene',
            id:"sceneId",
            name:"sceneId",
            valueField:"id",
            textField:"sceneName",
            placeholder:{text:""},
            search: true,
            required: true,
            url:basePath+"/bill/blankvou/bvvouchertype/listVoutypeSceneValidByVouTid?id="+voucherTypeId 
        });
    }
    var initPrintMRTable=function(){
	    //生成列表
	    bt.bttable.init({
	        scrollX:true,
	        scrollY:"400px",
	    	elem: "#printMRTable",
	    	url: basePath+"/bill/blankvou/bvprintformat/matchRuleDatatable?printId="+$("#id").val()+"&opType=add&vouTid="+$("#voucherType").find('select').val(),
	    	buttons: [
	    	          {
	    	        	  text: '<i class="layui-icon">&#xe642;</i>'+bt.lang('common.addTr'),
	    	        	  action:function($button){
	    	        		  rowMR++;
	    	        		  var html='<tr>';
	    	        		  html+='<td><div class="selectCodePrintMR'+rowMR+'" ></div></td>';
	    	        		  html+='<td><input name="matchVal" class="tableinput layui-input" type="text" lay-verify="required|strlength" data-maxlength="50" /></td>';
	    	        		  html+='<td><div class="bt-biz-dict" data-group="bv_print_match_rule" data-name="matchRule" data-type="select" data-defaultvalue="01"></div></td>';
	    	        		  html+='<td><div class="bt-biz-dict" data-group="sf_yn" data-name="validSign" data-type="select" data-defaultvalue="1"></div></td>';
	    	        		  html+='<td><button data-type="del" class="layui-btn layui-btn-mini">'+bt.lang("common.button.delete")+'</button></td>';
	    	        		  html+='</tr>';
	    	        		  $("#printMRTable tbody").append(html);
	    	        		  initSelectCode('PrintMR'+rowMR);
			  	    	      dicts.render();
	    	        	  }
	    	          }
	    	          ],
		      columns:[
		               {
		            	   data:"code",
		            	   render:function(data, row){
		            		   var reInfo;
		            		   if(row>0){
		            			   reInfo='<div class="selectCode" data-defaultvalue="'+data.code+'" ></div>';
		            		   }else{
		            			   reInfo=data.matchVal+'<input name="code" type="hidden" value="vouTid">';
		            		   }
		            		   return reInfo;
		            	   }
		               },
		               {
		            	   data:"matchVal",
		            	   render:function(data,row){
		            		   var reInfo;
		            		   if(row>0){
		            			   reInfo='<input name="matchVal" value="'+data.matchVal+'" class="tableinput layui-input" type="text" lay-verify="required|strlength" data-maxlength="50" />';
		            		   }else{
		            			   reInfo=data.vouTidName+'<input name="matchVal" type="hidden" value="'+data.vouTid+'">';
		            		   }
		            		   return reInfo;
		            	   }
		               },
		               {
		            	   data:"matchRule",
		            	   render:function(data,row){
		            		   var reInfo;
		            		   if(row>0){
		            			   reInfo='<div class="bt-biz-dict" data-group="bv_print_match_rule" data-name="matchRule" data-type="select" data-defaultvalue="01"></div>';
		            		   }else{
		            			   reInfo=data.matchRuleName+'<input name="matchRule" type="hidden" value="'+data.matchRule+'">';
		            		   }
		            		   return reInfo;
		            	   }
		               },
		               {
		            	   data:"validSign",
		            	   render:function(data,row){
		            		   var reInfo;
		            		   if(row>0){
		            			   reInfo='<div class="bt-biz-dict" data-group="sf_yn" data-name="validSign" data-type="select" data-defaultvalue="1"></div>';
		            		   }else{
		            			   reInfo=data.validSignName+'<input name="validSign" type="hidden" value="'+data.validSign+'">';
		            		   }
		            		   return reInfo;
		            	   }
		               },
		               {data:null,render:function(data,row){
		            	   var reInfo='';
	            		   if(row>0){
	            			   reInfo='<button data-type="del" class="layui-btn layui-btn-mini">'+bt.lang("common.button.delete")+'</button>';
	            		   }
		                   return reInfo;            
		               }}
		       ],            
		       delRowCallback:function(data,$tr){
		           $tr.remove();
		       },            
		       tableInitCallback:function(){  
		    	   initSelectCode();
 	    	    	dicts.render();
	          }
	    });
	    rowMR=0;
    }

    var initPrintFmtDtlTable=function(){
	    //生成列表
	    bt.bttable.init({
	        scrollX:true,
	        scrollY:"400px",
	    	elem: "#printFmtDtlTable",
	    	url: basePath+"/bill/blankvou/bvprintformat/printFmtDtlDatatable?printId="+$("#id").val(),
	        buttons: [
	            {
	                text: '<i class="layui-icon">&#xe642;</i>'+bt.lang('common.addTr'),
	                action:function($button){
	                	rowFmtDtl++;
	                	var html='<tr>';
		        		  html+='<td><div class="selectCode'+rowFmtDtl+'" </div></td>';
		        		  html+='<td><input name="name" class="layui-input" type="text" lay-verify="required" maxlength="50" style="width:80px;" /></td>';
		        		  html+='<td><input name="printCoord" class="layui-input" type="text" lay-verify="required|coordVfy" value="0,0" style="width:80px;" /></td>';
		        		  html+='<td><div class="bt-biz-dict" data-group="sf_yn" data-name="isPrint" data-type="select" data-defaultvalue="1" data-width="55px"></div></td>';
		        		  html+='<td><input type="text" id="fontSizeadd'+rowFmtDtl+'" data-name="fontSize" data-decimal="0" autocomplete="off" class="layui-input bt-biz-moneyType" style="width:50px;"/></td>';
		        		  html+='<td><input type="text" id="fontSpaceadd'+rowFmtDtl+'" data-name="fontSpace" data-decimal="0" autocomplete="off" class="layui-input bt-biz-moneyType" style="width:50px;"/></td>';
		        		  html+='<td><input type="text" id="charSizeBradd'+rowFmtDtl+'" data-name="charSizeBr" data-decimal="0" autocomplete="off" class="layui-input bt-biz-moneyType" style="width:50px;"/></td>';
		        		  html+='<td><input name="prefixVal" class="tableinput layui-input" type="text" style="width:50px;" lay-verify="strlength" data-maxlength="20" /></td>';
		        		  html+='<td><input name="otherVal" class="tableinput layui-input" type="text" style="width:80px;" lay-verify="strlength" data-maxlength="50" /> </td>';
		        		  html+='<td><div class="bt-biz-dict" data-group="bv_val_print_type" data-name="valPrintType" data-type="select" data-defaultvalue="01" data-width="100px"></div></td>';
		        		  html+='<td><div class="bt-biz-dict" data-group="bv_val_print_fmt" data-name="valPrintFmt" data-type="select" data-width="100px" ></div></td>';
		        		  html+='<td><button data-type="del" class="layui-btn layui-btn-mini">'+bt.lang("common.button.delete")+'</button></td>';
		        		  html+='</tr>';
		        		  $("#printFmtDtlTable tbody").append(html);
		  	    	   	initdataTableInt(rowFmtDtl);
		  	    	    initSelectCode(rowFmtDtl);
		  	    	    	dicts.render();
	                }
	            }
	        ],
	        columns:[
		  				{
		  					data:"code",
		  					render:function(data,row){
		  						var reInfo='<div class="selectCode" data-defaultvalue="'+data.code+'" ></div>';
		  						return reInfo;
				    		}
		  				},
		  				{
		  					data:"name",
		  					render:function(data,row){
		  						var reInfo='<input name="name" value="'+data.name+'" class="tableinput layui-input" type="text" lay-verify="required" />';
		  						return reInfo;
				    		}
				    	},
		  				{
		  					data:"printCoord",
		  					render:function(data,row){
		  						var reInfo='<input name="printCoord" value="'+data.printCoord+'" class="tableinput layui-input" type="text" lay-verify="required|coordVfy" />';
		  						return reInfo;
				    		}
				    	},
		  				{
		  					data:"isPrint",
		  					render:function(data,row){
		  						var reInfo='<div class="bt-biz-dict" data-group="sf_yn" data-name="isPrint" data-type="select" data-defaultvalue="1"></div>';
		  						return reInfo;
				    		}
				    	},
		  				{
		  					data:"fontSize",
		  					render:function(data,row){
		  						var reInfo='<input type="text" id="fontSize'+row+'" data-name="fontSize" value="'+data.fontSize+'" data-decimal="0" autocomplete="off" class="layui-input bt-biz-moneyType"/>';
		  						return reInfo;
				    		}
				    	},
		  				{
		  					data:"fontSpace",
		  					render:function(data,row){
		  						var reInfo='<input type="text" id="fontSpace'+row+'" data-name="fontSpace" value="'+data.fontSpace+'" data-decimal="0" autocomplete="off" class="layui-inpu bt-biz-moneyType"/>';
		  						return reInfo;
				    		}
				    	},
		  				{
		  					data:"charSizeBr",
		  					render:function(data,row){
		  						var reInfo='<input type="text" id="charSizeBr'+row+'" data-name="charSizeBr" value="'+data.charSizeBr+'" data-decimal="0" autocomple data-decimal class="layui-input bt-biz-moneyType"/>';
		  						return reInfo;
				    		}
				    	},
		  				{
		  					data:"prefixVal",
		  					render:function(data,row){
		  						var reInfo='<input name="prefixVal" value="'+data.prefixVal+'" class="tableinput layui-input" type="text" lay-verify="strlength" data-maxlength="20" />';
		  						return reInfo;
				    		}
				    	},
		  				{
		  					data:"otherVal",
		  					render:function(data,row){
		  						var reInfo='<input name="otherVal" value="'+data.otherVal+'" class="tableinput layui-input" type="text" lay-verify="strlength" data-maxlength="50" />';
		  						return reInfo;
				    		}
				    	},
		  				{
		  					data:"valPrintType",
		  					render:function(data,row){
		  						var reInfo='<div class="bt-biz-dict" data-group="bv_val_print_type" data-name="valPrintType" data-type="select" data-defaultvalue="01" data-width="100px"></div>';
		  						return reInfo;
				    		}
				    	},
		  				{
		  					data:"valPrintFmt",
		  					render:function(data,row){
		  						var reInfo='<div class="bt-biz-dict" data-group="bv_val_print_fmt" data-name="valPrintFmt" data-type="select" data-defaultvalue="02" data-width="100px"></div>';
		  						return reInfo;
				    		}
				    	},
	                   {data:null,render:function(data,row){
	                       return '<button data-type="del" class="layui-btn layui-btn-mini">'+bt.lang("common.button.delete")+'</button>';            
	                   }}
		       ],            
		       delRowCallback:function(data,$tr){
		           $tr.remove();
		       },            
		       tableInitCallback:function(){  
		    	   	initdataTableInt();
		    	    initSelectCode();
		    	    dicts.render();
	           },
	           fixedColumns:{
	           	rightColumns:1
	           }
	    });
	    rowFmtDtl=0;
    }
    var initSelectCode=function(row){
    	var cls="";
    	if(row!= undefined){
    		cls=row
    	}
    	if($(".selectCode"+cls).length>0){
    		bt.select.init({
    			elem:'.selectCode'+cls,
    			name:"code",
    			valueField:"fieldCode",
    			textField:"fieldName",
    			search: true,
    			required: true,
    			disabled: false,
    			width:"100px",
    			url:basePath+"/bill/blankvou/bvvouchertype/listVoutypeDataFieldValidByVouTid?id="+$("#voucherType").find('select').val()       
    		});
    	}
    }
    var moneyType = bt.moneyType.init();
    var initdataTableInt=function(row){
    	moneyType.render();
    }
    initPrintMRTable();
    initPrintFmtDtlTable();

    getDataInfo=function(){
    	var printMRInfo=new Array();
        //获取表格所有行
        var trs = $("#printMRTable").children("tbody").children("tr");
        //遍历行数据
        trs.each(function(index,tr){
            var obj = {};
            var tds = $(tr).find("td");
            tds.each(function(i,td){
                var $span = $(td).children("span");
                var $input = $(td).find("input");
                var $select = $(td).find("select");
                if($input.length && !$select.length){
                    var text = $input.val();
                    var name = $input.attr("name");
                    obj[name] = text;
                }
                if($select.length){
                    var text = $select.val();
                    var name = $select.attr("name");
                    obj[name] = text;
                }
                if($span.length){
                    var text = $span.text();
                    var name = $span.attr("name");
                    obj[name] = text;
                }
                obj['ordernum']=index;
            })
        printMRInfo.push(obj);
        });
        var printFmtDtlInfo=new Array();
        //获取表格所有行
        trs = $("#printFmtDtlTable").children("tbody").children("tr");
        //遍历行数据
        trs.each(function(index,tr){
            var obj = {};
            var tds = $(tr).find("td");
            tds.each(function(i,td){
                var $span = $(td).children("span");
                var $input = $(td).find("input");
                var $select = $(td).find("select");
                if($input.length && !$select.length){
                	if($input.length>1){
                		$input.each(function(j,input){
                			var name = input.name;
                			if(name){
                                var text = input.value;
                                obj[name] = text;
                			}
                		});
                	}else{
                        var text = $input.val();
                        var name = $input.attr("name");
                        obj[name] = text;
                	}
                }
                if($select.length){
                    var text = $select.val();
                    var name = $select.attr("name");
                    obj[name] = text;
                }
                if($span.length){
                    var text = $span.text();
                    var name = $span.attr("name");
                    obj[name] = text;
                }
                obj['ordernum']=index;
            })
        printFmtDtlInfo.push(obj);
        });
        var dataInfo={fmtNo:$("#fmtNo").val(),
        		id:$("#id").val(),
        		fmtName:$("#fmtName").val(),
        		vouTid:$("#voucherType").find('select').val(),
        		sceneId:$("#voucherTypeScene").find('select').val(),
        		paperSize:$("#paperSize").val(),
        		validSign:$('input[name="valSign"]:checked').val(),
            	backgroundImage:$("#backgroundImage").val(),
        		leftBorder:$("#leftBorder").val(),
        		upBorder:$("#upBorder").val()};
        dataInfo.printMR=printMRInfo;
        dataInfo.printFmtDtl=printFmtDtlInfo;
        dataInfo=JSON.stringify(dataInfo);
        return dataInfo;
    }
});

var resetFun = function(){
	location.reload();
}

var printViewTest=function(){
	if(!fromObj.verifyFun('#fromVerify')){
        return false;
    }
	var action = "/bill/blankvou/bvprintvouopinfo/printView";
	var dataInfo=getDataInfo();
	bt.jquery.ajax({
    	type: "POST",
    	url: basePath + action,
       	data: dataInfo,//将对象序列化成JSON字符串
       	dataType:"json",
       	async: false, 
       	contentType : 'application/json;charset=utf-8', //设置请求头信息 
    	success: function(result){
    		printBillFormat(result,false,true);
    	},
    	error: function(res){
    	}
    });
}

var opPrintCoord=function(date,reText){
    if(reText&&date[0]&&date[0].printFmtDtl){
        // LODOP.PRINT_INITA(0,0,1,1,"凭证套打打印");
        var printInita= reText.match(/(LODOP.PRINT_INITA\(){1}.*(,"凭证套打打印"\);){1}/g);
        if(printInita&&printInita[0]){
            var paperSize=printInita[0].split(",");
            var paperSizeWidth=paperSize[2];
            var paperSizeHeight=paperSize[3];
            $("#paperSize").val(paperSizeWidth+","+paperSizeHeight);
        }

        // LODOP.ADD_PRINT_TEXT("4.85cm","16.521cm","0.799cm","0.701cm",MyDataKey0);
        var coordRes= reText.match(/(LODOP.ADD_PRINT_TEXT\(){1}.*,MyDataKey\d+\);/g);
        if(coordRes){
            var trs=$("#printFmtDtlTable").children("tbody").children("tr");
            for(i=0;i<coordRes.length;i++){
                var coordReInfo=coordRes[i].split("\"");
                var coordLeft=parseInt(coordReInfo[3].split("cm")[0]*100);
                var coordRight=parseInt(coordReInfo[1].split("cm")[0]*100);
                var line=coordReInfo[8];
                line=parseInt(line.substring(10,line.length-2));
                trs.eq(line).find("td").eq(2).find("input[name='printCoord']").val(coordLeft+','+coordRight);
            }
        }
    }
}
