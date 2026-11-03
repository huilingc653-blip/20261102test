/**
 * 银行帐号格式化组件

 * 本组件依赖jquery
 * liuyj
 * 2017年7月4日
 * 0.0.1 多选下拉框
 * 0.0.2 修改样式及禁用时不可选择
 * 0.0.3 新增加reset方法；另外修改了itemsdisabled名称
 * 0.0.4 去掉url中的base
 * 0.0.5 在datatable中，使用此控件，需要重新计算列宽
 * 0.0.6 添加render传参，用于dict组件
 * 0.0.7 添加attr方法，重置下拉框参数
 * 0.0.8 render方法，添加了下拉框参数 用于设置重置
 * 0.0.9 删除input框中‘全选’字样
 * 0.0.10 新增method参数
 * 0.0.11 新增初始化完成回调
 * 0.0.12 disabled时，给select添加disabled，input删除校验规则
 * 0.0.13 新增search参数
 */
bt.define(['jquery','form'], function(exports){
	var $ = bt.jquery,form = bt.form();
	var Mulselect = function(){
		this.currentObject = null;
	};

	Mulselect.prototype.render = function(ops){
		var that = this;
		if(!ops || ops == 'reset'){ //重置时
			var settings = {};
			var domObj = $('.bt-biz-mulselectType:not(.init)');
			bt.each(domObj,function(){
				var $container = $(this);
				var url = $container.data("url");
				var valueField = $container.data('valueField'.toLowerCase()),
					name = $container.data('name'),
					textField = $container.data('textField'.toLowerCase()),
					disabled = $container.data('disabled'),
					defaultvalue = $container.data("defaultValue".toLowerCase()),//默认值
					required = $container.data("required"),
					bizName = $container.data("biz-name"),
					filterData = $container.data("filterData".toLowerCase()),
					displayData = $container.data("displayData".toLowerCase()),
					itemDisabled = $container.data("itemsdisabled".toLowerCase()),
					value = $container.data("value"),//最新值置空
					placeholder = $container.data("placeholder"),
					method = $container.data("method"),
					beforeInit = $container.data("beforeInit".toLowerCase()),
					search = $container.data("search"),
					callback = $container.data("callback"),
					initCompleteCallback = $container.data('initCompleteCallback'.toLowerCase());//初始化完成回调

				callback = that.makeCallBack(callback);
				beforeInit = that.makeCallBack(beforeInit);
				initCompleteCallback = that.makeCallBack(initCompleteCallback);
				var selectValue;
				if(ops == 'reset'){ //重置时优选择默认值；其它情况优先选择value;
					selectValue = defaultvalue;
				}else{
					selectValue = value ? value:defaultvalue;
				}

				settings = {
					elem:$container,
					url:(basePath?'':basePath)+url,
					name:name,
					valueField:valueField,
					textField: textField,
					disabled:disabled?true:false,
					defaultvalue:selectValue,
					required:required?true:false,
					placeholder:placeholder?placeholder:bt.lang('common.placeholder.select'),
					filterData:filterData?filterData:"",
					displayData:displayData?displayData:undefined,
					itemDisabled:itemDisabled?itemDisabled:"",
					callback:callback,
					method:method?method:"post",
					search:search?true:false,
					beforeInit:beforeInit,//初始化完成回调
					initCompleteCallback:initCompleteCallback//初始化完成回调
				};
				$container.addClass("init")
				that.getData($container,settings);
			})
		}else{
			that.getData(ops.elem,ops);
		}
	}

	//生成dls格式化
	Mulselect.prototype.setDls = function(a,$elem,data,obj) {
		var _this = this;
		var styles = {
			color:'#303343',
			backgroundColor:'#fff'
		}
		var sysSkin = bt.getSysSkin();
		if(sysSkin === 'dark-skin'){
			styles.color = '#d3d3d3';
			styles.backgroundColor = '#25262B'
		}
		// a(".layui-form select[multiple=multiple]").each(function(k, e) {
		$elem.find("select[multiple=multiple]:not(.initSelect)").each(function(k, e) {
			var f = "",
				d = "",
				c = "";
			a(this).find("option").each(function(h, b) {
				if (0 != h || null != a(this).attr("val") && "" !== a(this).attr("val") ) {
					h = a(this).attr("value");
					b = a(this).text();
					var g = a(this).attr("img"),
						e = a(this).is(":selected"),
						t = a(this).is(":disabled")

					f += '<dd lay-value="' + h + '" class="' + (e ? "layui-this" : "") + (t ?"layui-disabled" :"")+ '" style="color:'+styles.color+';background-color:'+styles.backgroundColor+';">';
					f += '<i class="fa '+(e ? "fa-check-square-o" : "fa-square-o")+' fa-lg "  style="margin-top:8px;margin-right:5px;"></i>';
					null != g && "" != g && (g = eval("(" + g + ")"), null != g.src && "" != g.src && (f += '<img src="' + g.src + '" height="16" width="16" style="margin-right:5px;vertical-align:middle;' + g.css + '"/>'));
					f += b;
					// f += '<i class="fa '+(e ? "fa-check" : "") + ' fa-lg" style="float:right;margin-top:8px;"></i>';

					f += "</dd>";
					if(h == 'all'){
						return
					}
					e && (d += "," + h, c += "," + b)
				}

			});
			"" != d && (d = d.substring(1));
			"" != c && (c = c.substring(1));
			a(this).siblings("div.layui-form-select").find("dl").html(f).addClass('bt-multiple-select');
			/*a(this).siblings("div.layui-form-select").find("dl").css("top","32px");*///多选下拉框在页面底部时无法显示全部，原因是这行代码固定位置了
			a(this).before('<input type="hidden" class="txtSel" name="' + a(this).attr("name") + '" value="' + d + '">');
			a(this).removeAttr("name");

			a(this).siblings("div.layui-form-select").find(".layui-select-title input").val(c)
			//.attr("lay-verify", a(this).attr("lay-verify"));
			a(this).siblings("div.layui-form-select").find("dd").each(function(e, b) {
				a(this).click(function(ev) {
					c = d = "";
					var b = {};
					var that = this;
					if(a(this).attr("lay-value")=="all"){
						a(this).parent().find('dd:not(dd[lay-value="all"],.layui-disabled)').each(function() {
							var value = a(this).attr("lay-value");

							if(a(that).hasClass("layui-this")){
								a(this).parent().parent().siblings("select").find('option[value="'+value+'"]').prop('selected',false);
								a(this).removeClass("layui-this").find("i").removeClass("fa-check-square-o").addClass("fa-square-o");

							}else{
								a(this).parent().parent().siblings("select").find('option[value="'+value+'"]').prop('selected',true);
								a(this).addClass("layui-this").find("i").addClass("fa-check-square-o").removeClass("fa-square-o");
							}
						});
						if(a(that).hasClass("layui-this")){
							a(that).parent().parent().siblings("select").find('option[value="all"]').prop('selected',false);
							a(that).removeClass("layui-this").find("i").removeClass("fa-check-square-o").addClass("fa-square-o");
						}else{
							a(that).parent().parent().siblings("select").find('option[value="all"]').prop('selected',true);
							a(that).addClass("layui-this").find("i").addClass("fa-check-square-o").removeClass("fa-square-o");
						}
					}else{
						if(!a(this).hasClass("layui-disabled")){
							var v = a(this).attr("lay-value");
							if(a(this).hasClass("layui-this")){
								a(this).parent().parent().siblings("select").find('option[value="'+v+'"]').prop('selected',false);
								a(this).removeClass("layui-this").find("i").removeClass("fa-check-square-o").addClass("fa-square-o")
							}else{
								a(this).parent().parent().siblings("select").find('option[value="'+v+'"]').prop('selected',true);
								a(this).addClass("layui-this").find("i").addClass("fa-check-square-o").removeClass("fa-square-o");
							}
						}
						var ddsSelectLength = a(this).parent().find('dd.layui-this:not(dd[lay-value="all"])').length;
						var ddsLength = a(this).parent().find('dd:not(dd[lay-value="all"],.layui-disabled)').length;
						if(ddsSelectLength == ddsLength){
							a('dd[lay-value="all"]').addClass("layui-this").find("i").addClass("fa-check-square-o").removeClass("fa-square-o");;
						}else{
							a('dd[lay-value="all"]').removeClass("layui-this").find("i").removeClass("fa-check-square-o").addClass("fa-square-o");
						}
					}
					a(this).parent().find("dd.layui-this").each(function() {
						d += "," + a(this).attr("lay-value");
						c += "," + a(this).text().substring(0)
					});
					"" != d && (d = d.substring(1));
					"" != c && (c = c.substring(1));

					//删除input中的全选字样
					if(a(this).parent().find('dd[lay-value= "all"]').hasClass('layui-this')){
						var cIndex = c.indexOf(',')+1;
						c = c.substring(cIndex,c.length);
						var dIndex = d.indexOf(',')+1;
						d = d.substring(dIndex,d.length);
					}

					a(this).parent().parent().siblings("input.txtSel").val(d);
					a(this).parent().parent().parent().data("value",d);
					a(this).parent().siblings(".layui-select-title").find("input.layui-input").val(c);
					_this.clickCallback(d,c,data,obj);

					_this.stopBubble(ev);
				})
			})
		})
	};
	/**
	 事件阻止冒泡
	 **/
	Mulselect.prototype.stopBubble = function(e) {
		var evt = e||window.event;
		evt.stopPropagation?evt.stopPropagation():(evt.cancelBubble=true);
		evt.preventDefault();
	};
	/**
	 * 获取html字符串
	 * @param {Object} data
	 */
	Mulselect.prototype.getHtml = function(data,o) {
		var that = this,_config = o;
		var selectHtml ='';
		if(_config.name){
			selectHtml += '<select name="'+_config.name +'"  ';
		}else{
			throwError('error: name参数未定义或设置出错，具体设置格式请参考文档API.')
		}

		if(_config.required){
			selectHtml += 'lay-verify="required"  class ="layui-input"'+' '
		}

		if ( _config.disabled ) {
			selectHtml +='disabled '
		}

		if (_config.search){
			selectHtml +='lay-search '
		}
		selectHtml +='multiple="multiple">'

		if(data){
			selectHtml += '<option value="">'+_config.placeholder+'</option>';
			if (typeof _config.defaultvalue === 'number' && !isNaN(_config.defaultvalue)){ //如果是数字
				selectHtml += '<option value="all">'+bt.lang('common.select.all')+'</option>'
			}else {
				//解决
				var isSelectedAll = false;
				if(data && Array.isArray(data) && _config.defaultvalue && typeof _config.defaultvalue === 'string'){
					isSelectedAll = _config.defaultvalue.split(',').length === data.length;
				}

				if( _config.defaultvalue && _config.defaultvalue.indexOf('all') !== -1 || isSelectedAll){
					selectHtml += '<option value="all" selected>'+bt.lang('common.select.all')+'</option>'
				}else {
					selectHtml += '<option value="all">'+bt.lang('common.select.all')+'</option>'
				}
			}
		}
		var itemsdisabled = _config.itemDisabled;
		var disabledArr = [];

		if(itemsdisabled){
			itemsdisabled = itemsdisabled.toString();
			if(itemsdisabled.indexOf(',') !=-1){
				disabledArr = itemsdisabled.split(",");
			}else{
				disabledArr.push(itemsdisabled)
			}
		}


		//过滤不需要显示的数据
		var filterData = _config.filterData;
		var filterArr =[];
		if(filterData != undefined){
			filterData = filterData.toString();
			if(filterData.indexOf(',') !==-1){
				filterArr = filterData.split(',');
			}else{
				filterArr.push(filterData)
			}
		}

		var displayData = _config.displayData
		var displayArr =[];
		if(displayData != undefined){
			displayData = displayData.toString();
			if(displayData.indexOf(',') !==-1){
				displayArr = displayData.split(',');
			}else{
				displayArr.push(displayData)
			}
		}

		var defaultvalue = _config.defaultvalue;

		if(typeof defaultvalue ==="number" ){
			var defaultvalue =defaultvalue.toString();
		}
		if(defaultvalue !==undefined){
			var defaultvalueArr =defaultvalue.split(",");
		}
		for(var i = 0;i <data.length;i++){
			var flag=false;
			var obj = data[i];
			for(var j = 0; j < filterArr.length ; j++){
				if(filterArr[j] == obj[_config.valueField]){
					flag = true;
					break;
				}
			}
			if(flag){
				continue;
			}

			var displayFlag = false;
			if(displayArr.length == 0){
				displayFlag = true
			}else{
				for(var j = 0; j < displayArr.length ; j++){
					if(displayArr[j] == obj[_config.valueField]){
						displayFlag = true;
						break;
					}
				}
			}
			if(!displayFlag){
				continue;
			}

			selectHtml +='<option value="'+obj[_config.valueField] +'" ';
			bt.each(defaultvalueArr,function(index,item){
				if(item ==obj[_config.valueField] || item == 'all'){
					selectHtml +='selected ';
				}
			})
			bt.each(obj,function(key,value){
				if(_config.valueField !== value && key!=="disabled"){
					selectHtml +='data-'+key +'= "'+value+'"  ';
				}
				if(key==="disabled" && value ){
					selectHtml += 'disabled  ';
				}
				bt.each(disabledArr,function(i,v){
					if(_config.valueField == key && v == value ){
						selectHtml += 'disabled  ';
					}
				})
			})

			selectHtml +=">"+obj[_config.textField]+'</option>';
		}
		selectHtml +='</select>';
		return selectHtml;
	};

	Mulselect.prototype.getData = function($elem,obj){
		var that = this, _config = obj;
		if(_config.data && typeof _config.data ==="object" && _config.data.length !==0){
			//var data = _config.data;
			var data = _config.beforeInit?_config.beforeInit(_config.data):_config.data;
			that.createMulselect(data,$elem,obj)
		}else{
			if(_config.url != undefined){
				if(_config.method =="post"){
					$.post(_config.url,_config.urldata,function(data){
						var data = _config.beforeInit?_config.beforeInit(data):data;
						that.createMulselect(data,$elem,obj);
						if(_config.initCompleteCallback){
							_config.initCompleteCallback(data);
						}
					},"json")
				}
				else if(_config.method =="get"){
					$.get(_config.url,_config.urldata,function(data){
						var data = _config.beforeInit?_config.beforeInit(data):data;
						that.createMulselect(data,$elem,obj);
						if(_config.initCompleteCallback){
							_config.initCompleteCallback(data);
						}
					},"json")
				}
			}
		}
	}

	//生成多选下拉框
	Mulselect.prototype.createMulselect = function (data,$elem,obj) {
		var that = this;
		if(!data){
			return;
		}
		var html = that.getHtml(data,obj);
		$elem.html(html);
		form.render('select');
		that.setDls($,$elem,data,obj)
		$elem.find("select").addClass("initSelect");
		var disabled = obj.disabled;
		if(disabled){
			$elem.find(".layui-form-select").children("div").removeClass("layui-select-title").find("input").addClass("layui-disabled")
			$elem.find(".layui-form-select").addClass("layui-select-disabled")
		}
		//在datatable中，使用此控件，需要重新计算列宽
		if($.fn.dataTable){
			$.fn.dataTable.tables( {visible: true, api: true} ).columns.adjust();
		}
	}

	Mulselect.prototype.makeCallBack = function(callBack){

		if(callBack && typeof(callBack) != "function"){
			try {
				callBack = eval(callBack);
			} catch(e) {
				alert(callBack + 'not exist！');
			}

		}
		return callBack;
	};

	Mulselect.prototype.clickCallback = function(d,c,data,obj){
		var callback = obj.callback;
		var valueField = obj.valueField;
		var valArr = d.split(',');
		var backArr =[];
		bt.each(valArr,function(i,v){
			bt.each(data,function(j,dataObj){
				if(v == dataObj[valueField]){
					backArr.push(dataObj)
				}
			})
		})
		if(callback && typeof callback ==="function"){
			callback(backArr,obj)
		}
	}

	Mulselect.prototype.val = function(obj){
		var that = this;
		if(!obj){
			if(!that.currentObject){
				return "";
			}
			var dataArr =[];
			var value = that.currentObject.find("input[type='hidden']").val();
			if(value === undefined){
				return dataArr;
			}
			value = value.toString();
			var vauleArr = value.split(",");


			bt.each(vauleArr,function(i,v){
				var option = that.currentObject.find('select option[value="'+v+'"]').data();
				dataArr.push(option)
			})

			return dataArr;

		}else{
			if(typeof obj !=="object"){
				throwError(obj+"形参不是对象")
			};

			bt.each(obj,function(key,value){
				that.currentObject.data(key,value);
			})
			that.currentObject.html("").removeClass("init");
			that.render();
		}
	}

	Mulselect.prototype.reset = function(){
		var that = this;
		if(!that.currentObject){
			return "";
		}
		that.currentObject.html("").removeClass("init");
		that.render()
	}

	Mulselect.prototype.attr = function(obj){
		var that = this;
		var currentObject = that.currentObject;
		if(!currentObject){
			return "";
		}
		if(obj !== undefined && typeof obj ==='object'){
			for(var key in obj){
				var attrName;
				if(key.indexOf("data-") !== -1){
					attrName= key.replace("data-",'');
				}else{
					attrName = key;
				}
				currentObject.data(attrName.toLowerCase(),obj[key]);
			}
			that.currentObject.html("").removeClass("init");
			that.render()
		}
	}

	function throwError(msg) {
		throw new Error("[bt-biz-mulselectType:" + msg);
	};

	var mulselect = new Mulselect();

	var obj = {
		init:function(ops){
			mulselect.render(ops);
			return this;
		},
		get:function(bizName){
			mulselect.currentObject = $('[data-biz-name="'+bizName+'"]');
			if(mulselect.currentObject.length===0){
				throwError(bizName+"尚未配置data-biz-name属性！")
			}
			return mulselect
		},
		render:function(ops){
			mulselect.render(ops)
		},
	}
	//输入口
	exports('mulselect', obj);
})
