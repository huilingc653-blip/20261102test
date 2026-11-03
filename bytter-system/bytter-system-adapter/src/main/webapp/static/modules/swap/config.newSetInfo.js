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

    // 监听提交
    form.on('submit(submit)', function(data){
    	bt.loading.show();
        var action = "/erp/swap/interconfigfill/save";
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


    var rowDtl=0;


    var initfillTable=function(){
	    //生成列表
	    bt.bttable.init({
	        scrollX:false,
	        scrollY:"400px",
	    	elem: "#fillTable",
	    	url: basePath+"/erp/swap/interconfig/setDTLInfoDatatable?type="+$("#configType").val(),
	        buttons: [
	            {
	                text: '<i class="layui-icon">&#xe642;</i>'+bt.lang('common.addTr'),
	                action:function($button){
	                	rowDtl++;
	                	var html='<tr>';
						html+='<td><div class="selectCode'+rowDtl+'" </div></td>';
						html+='<td><input name="showName" class="layui-input" type="text" lay-verify="required" /></td>';
                        html+='<td><div class="selectSource'+rowDtl+'" </div></td>';
                        html+='<td><input type="text" data-name="orderNum" data-decimal="0" data-min="1" autocomplete="off" lay-verify="required" class="layui-input bt-biz-moneyType" /></td>';
                        // html+='<td><div class="bt-biz-dict" data-group="sf_yn" data-name="isInput" data-type="select" data-defaultvalue="1" ></div></td>';
                        html+='<td><div class="bt-biz-dict" data-group="sf_yn" data-name="isNotNull" data-type="select" data-defaultvalue="1" ></div></td>';
                        html+='<td><div class="bt-biz-dict" data-group="sf_yn" data-name="validSign" data-type="select" data-defaultvalue="1"></div></td>';
						html+='<td><button data-type="del" class="layui-btn layui-btn-mini">'+bt.lang("common.button.delete")+'</button></td>';
						html+='</tr>';
						$("#fillTable tbody").append(html);
                        initdataTableInt();
		  	    	    initSelectCode(rowDtl);
						dicts.render();
	                }
	            }
	        ],
	        columns:[
		  				{
		  					data:"fieldCode",
		  					render:function(data,row){
		  						var reInfo='<div class="selectCodeClo'+row+'" data-defaultvalue="'+data.fieldCode+'" ></div>';
		  						return reInfo;
				    		}
		  				},
		  				{
		  					data:"showName",
		  					render:function(data,row){
		  						var reInfo='<input name="showName" value="'+data.showName+'" class="tableinput layui-input" type="text" lay-verify="required" />';
		  						return reInfo;
				    		}
				    	},

						{
							data:"dataSource",
							render:function(data,row){
								var reInfo='<div class="selectSourceClo'+row+'" data-defaultvalue="'+data.dataSource+'" ></div>';
								return reInfo;
							}
						},
						{
							data:"orderNum",
							render:function(data,row){
								var reInfo='<input type="text" id="orderNum'+row+'" data-name="orderNum" data-min="1" value="'+(data.orderNum)+'" lay-verify="required"  data-decimal="0" autocomplete="off" class="layui-input bt-biz-moneyType"/>';
								return reInfo;
							}
						},
						// {
						// 	data:"isInput",
						// 	render:function(data,row){
						// 		var reInfo='<div class="bt-biz-dict" data-group="sf_yn" data-name="isInput" data-type="select" data-defaultvalue="'+data.isInput+'"></div>';
						// 		return reInfo;
						// 	}
						// },
						{
							data:"isNotNull",
							render:function(data,row){
								var reInfo='<div class="bt-biz-dict" data-group="sf_yn" data-name="isNotNull" data-type="select" data-defaultvalue="'+data.isNotNull+'"></div>';
								return reInfo;
							}
						},
		  				{
		  					data:"validSign",
		  					render:function(data,row){
		  						var reInfo='<div class="bt-biz-dict" data-group="sf_yn" data-name="validSign" data-type="select" data-defaultvalue="'+data.validSign+'"></div>';
		  						return reInfo;
				    		}
				    	},
	                   {data:null,render:function(data,row){

                          // var reInfo='<input name="id" value="'+data.id+'" class="tableinput layui-input" type="hidden" />';
	                       var reInfo='<button data-type="del" class="layui-btn layui-btn-mini">'+bt.lang("common.button.delete")+'</button>';
	                       return reInfo;
	                   }}
		       ],            
		       delRowCallback:function(data,$tr){
		           $tr.remove();
		       },
			   tableInitCallback:function(){
                   initdataTableInt();
                	var trs = $("#fillTable").children("tbody").children("tr");
					//遍历行数据
					trs.each(function(index,tr){
						initSelectCode('Clo'+index);
					});
                   dicts.render();
			   },
			   fixedColumns:{
				 rightColumns:1
			  }
			});
	    rowDtl=0;
    }
    //格式化数字
    var moneyType = bt.moneyType.init();
    var initSelectCode=function(row){
    	var cls="";
    	if(row!= undefined){
    		cls=row
    	}
    	if($(".selectCode"+cls).length>0){
    		bt.select.init({
    			elem:'.selectCode'+cls,
    			name:"fieldCode",
    			valueField:"fieldCode",
    			textField:"fieldName",
    			search: true,
    			required: true,
    			disabled: false,
    			width:"150px",
    			url:basePath+"/erp/swap/interconfigfill/queryErpDataTypeFieldValid?type="+$("#configType").val()
    		});
    	}

        if($(".selectSource"+cls).length>0){
            bt.select.init({
                elem:'.selectSource'+cls,
                name:"dataSource",
                valueField:"code",
                textField:"name",
                search: true,
                required: false,
                disabled: false,
                width:"150px",
                url:basePath+"/erp/swap/interconfigfill/queryListErpDataTypeValid"
            });
        }
    }
    var initdataTableInt=function(row){
        moneyType.render();
    }
    initfillTable();

    getDataInfo=function(){
        var configInfo=new Array();
        //获取表格所有行
        trs = $("#fillTable").children("tbody").children("tr");
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
            });
            isPush(obj,configInfo);
        });
        var dataInfo={type:$("#configType").val()};

        dataInfo.erpInterConfigFillList=configInfo;
        dataInfo=JSON.stringify(dataInfo);
        return dataInfo;
    };


    /**
	 *
     * @param obj
     * @param configInfo
     */
    var isPush = function (obj,configInfo) {
        var flag=true;
        Object.keys(configInfo).forEach(function(key){
			if(configInfo[key].fieldCode == obj.fieldCode){
                flag=false;
			}
        });
        if(flag){
        	configInfo.push(obj);
        }
    }
});
