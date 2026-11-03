bt.use([ 'form','jquery','layedit','select','dicts','dictGroup','bttable','uuid'], function() {
	$ = bt.jquery, form = bt.form();
	var dicts = bt.dicts.init();
	var dictGroup = bt.dictGroup.init();
		    //生成列表
		    bt.bttable.init({
		    	elem: "#fieldDataTable",
		    	url: basePath+"/bill/blankvou/bvvouchertype/fieldDataTable?vouTid="+$("#id").val()+"&opType=update",
		    	buttons: [
		    	          {
		    	        	  text: '<i class="layui-icon">&#xe642;</i>'+bt.lang('common.addTr'),
		    	        	  action:function($button){
		    	        		  var html='<tr>';
		    	        		  html+='<td><input name="fieldCode" class="tableinput layui-input" type="text" onblur="checkFieldCode(this)" lay-verify="fieldRequired|checkFieldCode" /></td>';
		    	        		  html+='<td><input name="fieldName" class="tableinput layui-input" type="text" lay-verify="fieldRequired"/></td>';
		    	        		  html+='<td><div class="bt-biz-dict" data-group="bv_voutype_field_datatype" data-name="dataType" data-type="select" lay-verify="checkDataTypeUnique"></div></td>';
		    	        		  html+='<td><div class="bt-biz-dictGroup" data-name="dictGroup" ></div></td>';
		    	        		  html+='<td><div class="bt-biz-dict" data-group="sf_yn" data-name="validSign" data-type="select" data-placeholder="" data-defaultvalue="1"></div></td>';
		    	        		  html+='<td><div class="bt-biz-dict" data-group="sf_yn" data-name="isShow" data-type="select" data-placeholder="" data-defaultvalue="1"></div></td>';
		    	        		  html+='<td><button data-type="del" class="layui-btn layui-btn-mini">'+bt.lang("common.button.delete")+'</button></td>';
		    	        		  html+='</tr>';
		    	        		  $("#fieldDataTable tbody").append(html);
		    	        		  dicts.render();
		    	        		  dictGroup.render();
		    	        	  }
		    	          }
		    	          ],
			      columns:[
			               {
			            	   data:"fieldCode",
			            	   render:function(data, row){
			            		   var reInfo;
			            		   if(row<2){
			            			   reInfo=data.fieldCode+'<input name="fieldCode" type="hidden" value="'+data.fieldCode+'"></input>';
			            		   }else{
			            			   reInfo='<input name="fieldCode" class="tableinput layui-input" type="text" onblur="checkFieldCode(this)" lay-verify="fieldRequired|checkFieldCode" value="'+data.fieldCode+'"/>';   
			            		   }
			            		   return reInfo;
			            	   }
			               },
			               {
			            	   data:"fieldName",
			            	   render:function(data,row){
			            		   var reInfo;
			            		   if(row<2){
			            			   reInfo=bt.lang(data.fieldName)+'<input name="fieldName" type="hidden" value="'+data.fieldName+'"></input>';
			            		   }else{
			            			   reInfo='<input name="fieldName" class="tableinput layui-input" type="text" value="'+data.fieldName+'" lay-verify="fieldRequired"/>';
			            		   }
			            		   return reInfo;
			            	   }
			               },		               
			               {
			            	   data:"dataType",
			            	   render:function(data,row){ 
			            		   var reInfo;
			            		   if(row<2){
			            			   reInfo='<input name="dataType" type="hidden" value="">';
				            		}else{
				            		   reInfo='<div class="bt-biz-dict" data-group="bv_voutype_field_datatype" data-name="dataType" data-type="select" data-defaultvalue="'+data.dataType+'" lay-verify="checkDataTypeUnique"></div>';
			            	   }
			            		   return reInfo;
			            	   }				            	    
			               },           
			               {
			            	   data:"dictGroup",
			            	   render:function(data,row){ 
			            		   var reInfo;
			            		   if(row<2){
			            			   reInfo='<input name="dictGroup" type="hidden" value="">';
				            		}else{
				            		   reInfo='<div class="bt-biz-dictGroup" data-name="dictGroup" data-defaultvalue="'+data.dictGroup+'" ></div>';
				            		}
			            		   return reInfo;
			            	   }			
			               },            
			               {
			            	   data:"validSign",
			            	   render:function(data,row){
			            		   var reInfo;
			            		   if(row<2){
			            			   reInfo=data.validSignName+'<input name="validSign" type="hidden" value="'+data.validSign+'">';
				            		}else{
				            		   reInfo='<div class="bt-biz-dict" data-group="sf_yn" data-name="validSign" data-type="select" data-placeholder="" data-defaultvalue="'+data.validSign+'"></div>';
			            	   }
			            		   return reInfo;  
			            	   }		   
			            	  
			               },
			               {
			            	   data:"isShow",
			            	   render:function(data,row){
			            		   return '<div class="bt-biz-dict" data-group="sf_yn" data-name="isShow" data-type="select" data-placeholder="" data-defaultvalue="'+data.isShow+'"></div>';
			            	   }
			               },
			               {data:null,className:"center",
			            	   render:function(data,row){
			            	   var reInfo;
			            	   if(row<2){
			            		   reInfo='';
			            	   }else{
			            		   reInfo='<button data-type="del" class="layui-btn layui-btn-mini">'+bt.lang("common.button.delete")+'</button>';  
			            	   }
			            	   return reInfo; 
			               }}
			       ],            
			       delRowCallback:function(data,$tr){
			           $tr.remove();
			       },            
			       tableInitCallback:function(){
			    	   dicts.render();
			    	   dictGroup.render();
		          }
		    });

		    //生成列表
		    bt.bttable.init({
		    	elem: "#sceneDataTable",
		    	url: basePath+"/bill/blankvou/bvvouchertype/sceneDataTable?vouTid="+$("#id").val(),
		    	buttons: [
		    	          {
		    	        	  text: '<i class="layui-icon">&#xe642;</i>'+bt.lang('common.addTr'),
		    	        	  action:function($button){
		    	        		  var html='<tr>';
		    	        		  var uuid=bt.uuid.get();
		    	        		  html+='<td><input name="sceneId" type="hidden" value="'+uuid+'"><input name="sceneName" class="tableinput layui-input layui-inline" type="text" lay-verify="required" style="display:inline" /><span data-toggle="i18n" data-key-id="'+uuid+'"/></td>';
		    	        		  html+='<td><div class="bt-biz-dict" data-group="sf_yn" data-name="sceneValidSign" data-type="select" data-placeholder="" data-defaultvalue="1"></div></td>';
		    	        		  html+='<td><button data-type="del" class="layui-btn layui-btn-mini">'+bt.lang("common.button.delete")+'</button></td>';
		    	        		  html+='</tr>';
		    	        		  $("#sceneDataTable tbody").append(html);
		    	        		  dicts.render();
		    	        		  dictGroup.render();
		    	        		  i18nInit();
		    	        	  }
		    	          }
		    	          ],
			      columns:[
			               {
			            	   data:"name",
			            	   render:function(data, row){
		            			   reInfo='<input name="sceneId" type="hidden" value="'+(data.id||'')+'"></input><input name="sceneName" class="tableinput layui-input layui-inline" type="text" lay-verify="required" value="'+data.name+'" style="display:inline"/><span data-toggle="i18n" data-key-id="'+(data.id||'')+'"/>';   
			            		   return reInfo;
			            	   }
			            	   
			               },
			               {
			            	   data:"validSign",
			            	   render:function(data,row){ 
			            		   var reInfo;
			            		   if(row<1){
			            			   reInfo=data.validSignName+'<input name="sceneValidSign" type="hidden" value="'+data.validSign+'">';
				            		}else{
				            		   reInfo='<div class="bt-biz-dict" data-group="sf_yn" data-name="sceneValidSign" data-type="select" data-placeholder="" data-defaultvalue="'+data.validSign+'"></div>';
				            		}
			            		   return reInfo;  
			            	   }				            	   
			            	  
			               },
			               {data:null,className:"center",
			            	   render:function(data,row){			    
			            	   var reInfo;
			            	   if(row<1){
			            		   reInfo='';
			            	   }else{
			            		   reInfo='<button data-type="del" class="layui-btn layui-btn-mini" >'+bt.lang("common.button.delete")+'</button>';  
			            	   }
			            	   return reInfo; 
			               }
			              }
			       ],            
			       delRowCallback:function(data,$tr){
			           $tr.remove();
			       },            
			       tableInitCallback:function(){
			    	   dicts.render();
 	        		  i18nInit();
		          }
		    });
	//初始化	    
	dataSourceInit();		    
		    
	// 校验输入code是否重复
	form.verify({
		checkCode : function(value) {
			if(value){     //非空字符串
				var success = true;
				$.ajax({
					type : 'POST',
					url : basePath + "/bill/blankvou/bvvouchertype/checkVoucherTypeCode",
					data : {'opType' : 'update','code' :value,'id':$("#id").val()},
					async : false,
					dataType : 'json',
					success : function(result) {
						if (!result.success) {
							success =false;
						}
					}
				});
				if (!success) {
					return bt.lang('bill.message.code.unique');
				}
			}
			
		},	
		//校验数据源
		   checkDataSource:function(value){
			   var checkFlag=$("#dataSource").attr("checkFlag");
			   if(checkFlag=='false'&&value){
				   return bt.lang('bill.bvVoucherType.checkDataSourceFlag');
			   }
		    },
		   //校验取数字段代码
		  checkFieldCode:function(value,el){
			   var dataSource=$("#dataSource").val();
	           var checkFlag=$("#dataSource").attr("checkFlag");		  
	           var checkFieldCode=true;	   
	           var fieldCodeUnique=true;
			   if(checkFlag=='true'&&value){     
				   $.ajax({
					 type:'post',
					 url:basePath + "/bill/blankvou/bvvouchertype/isExistColumn",
					 data:{"fieldCode":value,"dataSource":dataSource},
					 async:false,
					 dataType:'json',
					 success:function(result){
						 if(result.success){
			    			   $("input[name='fieldCode']").each(function(m,n){
			    				  var fieldCodeOther=$(n).val();
			    				  if(!$(el).is(n)&&fieldCodeOther&&fieldCodeOther.toLowerCase()==value.toLowerCase()){
			    					fieldCodeUnique=false;  
			    				  } 
			    			   });
			    		   }else{
			    			   checkFieldCode=false;
			    		   } 
					 }
				});
				   if(!checkFieldCode){
					   return bt.lang("blankvou.message.error.checkfieldcode",[value,dataSource]);
				   }
				   if(!fieldCodeUnique){
					   return bt.lang("bill.bvVoucherType.checkFieldCodeUnique",[value]);
				   }
			   }
		     },
		    //校验数据类型必须唯一
		    checkDataTypeUnique:function(value,el){
		    	var typeValue=$(el).find("select").val();
		    	var checkFlag=$('#dataSource').attr("checkFlag");
		    	var typeUnique=true;
		    	if(checkFlag=='true'&&$.trim(typeValue)){
		    		$("select[name='dataType']").each(function(o,p){
		    			if(!$(p).is($(el).find("select"))&&typeValue==$(p).val()){
		    				typeUnique=false;
		    				return false;
		    			}
		    		})
		    		if(!typeUnique){
		    			return bt.lang("bill.bvVoucherType.checkDataTypeFlag");
		    		}
		    	}
		    },
		     //自定义required
		     fieldRequired:function(value){
		    	 var checkFlag=$("#dataSource").attr("checkFlag");
		    	 if(checkFlag=='true'&&!value){
		    		 return bt.lang("bill.bvVoucherType.fieldRequiredMsg");
		    	 }	    	
		     }
		  });

		// 提交保存数据
		form.on('submit(submit)', function(data) {
            bt.loading.show();
			var vouTypeData;
			// 组装表单数据
			var formObj = {
				"id":$("#id").val(),	
				"code" : $.trim(data.field.code),
				"name" : data.field.name,
				"isInbox" : data.field.isInbox,
				"validSign":data.field.bvValidSign,
				"dataSource" : $.trim(data.field.dataSource),
				"rmk" : data.field.rmk
			};

			vouTypeData=formObj;
			var fieldData = [];
			if( $.trim(data.field.dataSource)){      //不为空时进行取值
				fieldData = getListDate('fieldDataTable');
			}
			var sceneData = getListDate('sceneDataTable');
			
			$.ajax({
				type : 'post',
				url : basePath + "/bill/blankvou/bvvouchertype/modify",
				contentType : 'application/json; charset=UTF-8',
				data : JSON.stringify({
					'bvVoucherType' : vouTypeData,
					'bvVoutypeDataFieldList' : fieldData,
					'bvVoutypeSceneList' : sceneData
				}),
				success : function(result) {
                    bt.loading.hide();
					parent.layer.alert(result.msg,{icon:result.success?1:2});
					if (result.success) {
						bt.navtab.closeCurrent();
					}
				},
				dataType : 'json'
			});
			return false;
		})

		var getListDate=function(dataTable){
			var listData =[];
			var trs = $("#"+dataTable).children("tbody").children("tr");		
			// 遍历行数据
			trs.each(function(index, tr) {
				var obj = {};
				var tds = $(tr).find("td");
				tds.each(function(i, td) {
					var $span = $(td).children("span");
					var $input = $(td).find("input");
					var $select = $(td).find("select");
					if ($input.length && !$select.length) {
						for(var i=0;i<$input.length;i++){
							var text = $($input[i]).val();
							var name = $($input[i]).attr("name");
							if(name=='sceneName'){
								name='name';
							}else if(name=='sceneValidSign'){
								name='validSign';
							}else if(name=='sceneId'){
								name='id';
							}
							obj[name] = text;
						}
					}
					if ($select.length) {
						var text = $select.val();
						var name = $select.attr("name");
						if(name=='sceneName'){
							name='name';
						}else if(name=='sceneValidSign'){
							name='validSign';
						}else if(name=='sceneId'){
							name='id';
						}
						obj[name] = text;
					}
					if ($span.length&&$span.attr("name")) {
						var text = $span.text();
						var name = $span.attr("name");
						if(name=='sceneName'){
							name='name';
						}else if(name=='sceneValidSign'){
							name='validSign';
						}else if(name=='sceneId'){
							name='id';
						}
						obj[name] = text;
					}							
				})
				listData.push(obj);
			})
			return listData;
		}
		
		//重置时触发
		$('#reset').bind('click',function(){
			dataSourceInit();
		});
	});

	//校验输入表或视图是否存在
	function checkDataSource(){
		var dataSource=$("#dataSource").val();
		if(dataSource){
			$.ajax({
				    type:'post',
				    url:basePath+"/bill/blankvou/bvvouchertype/isExistTable",
				    data:{"dataSource":dataSource},
				    async:false,
				    success:function(result){
				    	if(result.success){
				    		$("#dataSource").attr("checkFlag","true"); 			    		
				    	}else{
				    		$("#dataSource").attr("checkFlag","false");
				    	    layer.alert(result.msg, {icon:result.success?1:2});	
				    	}
				    },
				    dataType:'json'
			})
		}else{
			$("#dataSource").attr("checkFlag","");
		}
	}

	//检验取值字段是否存在
	function checkFieldCode(dom){
		var dataSource=$("#dataSource").val();
		var checkFlag=$("#dataSource").attr("checkFlag");
		var fieldCodeUnique=true;
		var fieldCode=$(dom).val();
		if(checkFlag=='true'&&fieldCode){
		     $.ajax({
		    	 type:'post',
		    	 url:basePath+"/bill/blankvou/bvvouchertype/isExistColumn",
		    	 data:{"fieldCode":fieldCode,"dataSource":dataSource},
		    	 async:false,
		    	 success:function(result){
		    		   if(result.success){
	                       //校验取值字段是否重复
		    			   $("input[name='fieldCode']").each(function(m,n){
		    				   var fieldCodeOther=$(n).val();
		    				  if(!$(dom).is(n)&&fieldCodeOther&&fieldCodeOther.toLowerCase()==fieldCode.toLowerCase()){
                                       fieldCodeUnique=false;
		    				  }
		    			   });
		    		   }else{
		    			   layer.alert(result.msg, {icon:result.success?1:2});
		    		   } 
		    	 },
		    	 dataType:'json'
		     })
            if(!fieldCodeUnique){
                layer.alert(bt.lang("bill.bvVoucherType.checkFieldCodeUnique",[fieldCode]),{icon:7});
            }
		}
		
	}
	
	//取值字段属性初始化
	function dataSourceInit(){
		  //取数字段属性初始化
	    var initDataSource=$("#initDataSource").val();
		if(initDataSource){
			$("#dataSource").attr("checkFlag","true");
		}else{
			$("#dataSource").attr("checkFlag","");
		};	    
	}
	
	
	
	
	
	
	

