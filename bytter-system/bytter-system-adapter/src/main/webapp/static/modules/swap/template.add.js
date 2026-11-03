bt.use(['form','select','element','dicts','datatablecore','dateType','selectCallback'], function(){
    var $ = bt.jquery,form = bt.form(),element = bt.element();
    bt.dicts.init();
    var typeId = $("input[name='templateTypeId']").val();
    var autoHtml = "",wayAll = [{"name":bt.dict("FcCompareAcc.loanSend",1),"value":1},
        {"name":bt.dict("FcCompareAcc.loanSend",0),"value":0}]
    var rowAllData = {};//对象
    var openViewIndex = 0;//记录弹窗控件的总数
    var aOpenView=[];
    var bData=[];//存储凭证数据，用于新增行按钮，初始化弹窗控件

    //表头列表
    getTypeDtl(0,function(data){
        if(data){
            var h_html = "",t_html = "";
            var aIndex = 0;
            for(var i=0;i<data.length;i++){
                var inputTypeCode = data[i].inputTypeCode;//输入方式
                h_html+='<th>'+(data[i].notNull?'<span class="pl"><span class="required-icon">*</span></span>':'')+data[i].showName+'</th>';
                t_html+='<td class="center">';
                if(inputTypeCode==='2'){//下拉框
                    t_html+='<div class="slectheadsource init" code="'+data[i].fieldCode+'" data-required="'+(data[i].notNull?true:false)+'" data-code="'+data[i].sourceCode+'"></div>';
                }else if(inputTypeCode==='3'){//日期框
                    t_html+='<input type="text" '+(data[i].notNull?'lay-verify="required"':'')+' name="'+data[i].fieldCode+'" class="layui-input bt-biz-dateType" flag="head_sel_input">';
                }else if(inputTypeCode==='4'){//弹出框
                    t_html+='<div class="layui-inline"><input type="hidden" name="'+data[i].fieldCode+'" class="layui-input" id="HinputType'+openViewIndex+'" flag="head_sel_input"> \
                        <input type="text" '+(data[i].notNull?'lay-verify="required"':'')+' readonly class="layui-input" id="inputType'+openViewIndex+'">\
                        <div class="bt-div-search"><button class="bt-biz-inputType'+openViewIndex+'" type="button" \
                         data-callback="bt.selectValue" title="选择数据"><i class="layui-icon">&#xe615;</i></button></div></div>';

                    var dataMsg={"class":"inputType"+openViewIndex+"","inputCode":data[i].sourceCode};
                    aOpenView[aIndex++]=dataMsg;//存储每个字段初始化弹窗控件需要的值
                    openViewIndex++;
                }else{//文本框
                    t_html+='<input type="text" '+(data[i].notNull?'lay-verify="required|strlength" data-maxlength="100"':'lay-verify="strlength" data-maxlength="100"')+' name="'+data[i].fieldCode+'" class="layui-input" flag="head_sel_input">';
                }
                t_html+='</td>';
            }
            $("#head_thead").html('<tr>'+h_html+'</tr>');
            $("#head_table").html('<tr>'+t_html+'</tr>');
        }
        //渲染
        initSource("slectheadsource",0);
        //初始化弹窗组件
        initOpenView(aOpenView);
        aOpenView = [];//清空数据
    });
    //凭证列表
    getTypeDtl(1,function(data){
        if(data){
            bData = data;//存储凭证数据，用于新增行按钮，初始化弹窗控件
            var h_html = '<th>'+bt.lang("table.fcAccBalance.balanceWay")+'</th>',t_html = "";
            for(var i=0;i<data.length;i++){
                h_html+='<th>'+(data[i].notNull?'<span class="pl"><span class="required-icon">*</span></span>':'')+data[i].showName+'</th>';
            }
            $("#botton_thead").html('<tr>'+h_html+'<th>'+bt.lang("common.table.thead.operation")+'</th></tr>');
            if(data.length>0){
                for(var i=1;i>-1;i--){
                    t_html = addHtml(data);//新增行按钮，动态添加html
                    $("#botton_table").append('<tr class="nodel"><td class="center">' +
                        '<div class="selectWay" data-name="'+bt.dict("FcCompareAcc.loanSend",i)+'" data-value="'+i+'"></div></td>'+t_html+'<td class="center"></td></tr>');
                }
                initWay("selectWay");
            }
            //初始化数据来源
            initSource("slectsource",1);
            //初始化弹窗组件
            initOpenView(aOpenView);
            aOpenView = [];//清空数据
        }
    });

    $(document).on('click','.addtr',function(){
        var t_html = addHtml(bData);
        autoHtml = '<tr class="yesdel"><td class="center"><div class="selectWay initsel" ></div></td>'+t_html+'<td><a class="deleteColumn" href="javascript:;">'+bt.lang("common.button.delete")+'</a></td></tr>';
        $("#botton_table").append(autoHtml);
        bt.dateType.init();//初始化时间控件
        stopBubble();
        initAutoWay("selectWay");
        initAutoSource("slectsource",1);
    });
    //新增行按钮，动态添加html
    function addHtml(data){
        var bIndex = 0;
        var t_html = "";
        for(var i=0;i<data.length;i++){
            var inputTypeCode = data[i].inputTypeCode;//输入方式
            t_html+='<td class="center">';
            if(inputTypeCode==='2'){//下拉框
                t_html+='<div class="slectsource initsel" code="'+data[i].fieldCode+'" data-required="'+(data[i].notNull?true:false)+'" data-code="'+data[i].sourceCode+'"></div>';
            }else if(inputTypeCode==='3'){//日期框
                t_html+='<input type="text" '+(data[i].notNull?'lay-verify="required"':'')+' name="'+data[i].fieldCode+'" class="layui-input bt-biz-dateType" flag="head_sel_input">';
            }else if(inputTypeCode==='4'){//弹出框
                t_html+='<div class="layui-inline"><input type="hidden" name="'+data[i].fieldCode+'" class="layui-input" id="HinputType'+openViewIndex+'" flag="head_sel_input"> \
                        <input type="text" '+(data[i].notNull?'lay-verify="required"':'')+' readonly class="layui-input" id="inputType'+openViewIndex+'">\
                        <div class="bt-div-search"><button class="bt-biz-inputType'+openViewIndex+'" type="button" data-callback="bt.selectValue" \
                        title="选择数据"><i class="layui-icon">&#xe615;</i></button></div></div>';

                var dataMsg={"class":"inputType"+openViewIndex+"","inputCode":data[i].sourceCode};
                aOpenView[bIndex++] = dataMsg;//存储每个字段初始化弹窗控件需要的值
                openViewIndex++;
            }else{//文本框
                t_html+='<input type="text" '+(data[i].notNull?'lay-verify="required|strlength" data-maxlength="100"':'lay-verify="strlength" data-maxlength="100"')+' name="'+data[i].fieldCode+'" class="layui-input" flag="head_sel_input">';
            }
            t_html+='</td>';
        }
        //初始化弹窗组件
        initOpenView(aOpenView);
        aOpenView = [];//清空数据
        return t_html;
    }
    $(document).on('click','a.deleteColumn',function(){
        var _that = this;
        layer.alert(bt.lang('common.message.confirm.delete'), function () {
            layer.closeAll();
            if ($(_that).parents("tr.yesdel")) {
                $(_that).parents("tr.yesdel").remove();
            }
            stopBubble();
        })
    });

    function getTypeDtl(flag,fn) {
        $.ajax({
            type: "GET",
            url: basePath+"/erp/swap/templatetypedtl/show/"+typeId+"?ishow="+flag,
            dataType: 'json',
            success: function (result) {
                var data = [];
                if (result === null || !result) {
                    data = [];
                }else {
                    data = result;
                }
                if(fn){
                    fn(data);
                }
            }
        });
    }
    function initAutoWay(type) {
        var ways = $("."+type+".initsel");
        if(ways){
            for(var i=0;i<ways.length;i++){
                bt.select.init({
                    elem: $(ways[i]),
                    name: 'loanDirection',
                    valueField: "value",
                    textField: "name",
                    dataAttr:{
                        flag: "head_sel_input"
                    },
                    placeholder: {value:"",text:""},
                    data: wayAll
                });
                $(ways[i]).removeClass("initsel");
            }
        }
    }
    function initWay(type) {
        var ways = $("."+type);
        if(ways){
            for(var i=0;i<ways.length;i++){
                var name = $(ways[i]).data("name");
                var value = $(ways[i]).data("value");
                bt.select.init({
                    elem: $(ways[i]),
                    name: 'loanDirection',
                    defaultVal: value,
                    dataAttr:{
                        flag: "head_sel_input"
                    },
                    valueField: "value",
                    textField: "name",
                    placeholder: {value:"",text:""},
                    data:wayAll// [{"name":name,"value":value}]
                })
            }
        }
    }
    function initSource(type,save){
        var sources = $("."+type);
        if(sources){
            for(var i=0;i<sources.length;i++){
                var code = $(sources[i]).data("code");
                var fcode = $(sources[i]).attr("code");
                var required = $(sources[i]).data("required");
                bt.select.init({
                    elem: $(sources[i]),
                    name: fcode,
                    dataAttr:{
                        fieldCode: fcode,
                        flag: 'head_sel_input',
                        fieldSave: save
                    },
                    required: required,
                    valueField: "code",
                    textField: "name",
                    initCallBack: function (elem,data) {
                        var code = $(elem).data("fieldcode");
                        if(data && typeof data.success==="undefined"){
                            if(code && typeof rowAllData[code]==="undefined"){
                                rowAllData[code] = data;
                            }
                        }else{
                            if(code && typeof rowAllData[code]==="undefined"){
                                rowAllData[code] = [];
                            }
                            //var required = $(elem).parent("div").data("required");
                            //var fcode = $(elem).parent("div").attr("code");
                            //$(elem).parent("div").html('<input type="text" '+(required?'lay-verify="required"':'')+' name="'+fcode+'" class="layui-input" flag="head_sel_input">');
                        }
                    },
                    url: basePath+"/erp/swap/template/source?code="+code
                })
                $(sources[i]).removeClass("initsel");
            }
        }
    }
    function initAutoSource(type) {
        var sources = $("."+type+".initsel");
        if(sources){
            for(var i=0;i<sources.length;i++){
                var fcode = $(sources[i]).attr("code");
                var required = $(sources[i]).data("required");
                bt.select.init({
                    elem: $(sources[i]),
                    name: fcode,
                    dataAttr:{
                        fieldCode: fcode,
                        flag: "head_sel_input",
                        fieldSave: 1
                    },
                    required: required,
                    valueField: "code",
                    textField: "name",
                    data: rowAllData[fcode]
                })
                $(sources[i]).removeClass("initsel");
            }
        }
    }
    //初始化每个弹窗控件
    function initOpenView(index){
        var selectCallback = bt.selectCallback;
        bt.dateType.init();//初始化时间控件
        if(index.length>0){
            for(var i=0;i<index.length;i++){
                var caback = null;
                var inputCode = index[i].inputCode? index[i].inputCode:"-";
                var inputId = index[i].class? index[i].class:"-";
                var inputType0 =  {
                    init: function () {
                        caback = selectCallback.init({
                            name:index[i].class,
                            url:basePath + "/erp/swap/template/listInput/"+inputCode+"/"+inputId,
                            width:980,
                            hight:900
                        });
                        return this;
                    },
                    set:function(obj){
                        // if(!caback){
                        //     console.error("inputType组件未初始化")
                        // }
                        caback.set(index[i].class,obj);
                    }
                };
                inputType0.init();
            }
        }
        //弹窗选择数据回调函数
        bt.selectValue = function (data, dom) {
            $("#H"+data.inputId).val(data.code);
            $("#"+data.inputId).val(data.name);
        }
    }

    var stopBubble = function(e) {
        var evt = e||window.event;
        evt.stopPropagation?evt.stopPropagation():(evt.cancelBubble=true);
        evt.preventDefault();
    };
    var isEmptyObject = function (obj) {
        for (var key in obj){
            return false;
        }
        return true;
    }
    form.on('submit(submit)', function(data){
        data['field']['map'] = {};
        data['field']['list'] = [];
        //组合数据
        var inputs = $("#head_table input[flag='head_sel_input']");
        var selects = $("#head_table select[data-flag='head_sel_input']");
        for(var i=0;i<inputs.length;i++){
            var name = $(inputs[i]).attr("name");
            var val = $(inputs[i]).val();
            if(name){
                data['field']['map'][name] = val;
            }
        }
        for(var i=0;i<selects.length;i++){
            var name = $(selects[i]).attr("name");
            var val = $(selects[i]).find("option:selected").val();
            if(name){
                data['field']['map'][name] = val;
            }
        }
        //组合行数据
        var btr_list = $("#botton_table tr");
        var trLD = [];
        var jie = false,dai = false;
        for(var j=0;j<btr_list.length;j++){
            var ips = $(btr_list[j]).find("input[flag='head_sel_input']");
            var is = $(btr_list[j]).find("select[data-flag='head_sel_input']");
            var o = {};
            for(var i=0;i<ips.length;i++){
                var name = $(ips[i]).attr("name");
                var val = $(ips[i]).val();
                if(name){
                    o[name] = val;
                }
            }
            for(var i=0;i<is.length;i++){
                var name = $(is[i]).attr("name");
                var val = $(is[i]).find("option:selected").val();
                if(name){
                    o[name] = val;
                }
                if(name==="loanDirection"){
                    trLD[j] = val;
                    if(val==='0') dai = true;
                    if(val==='1') jie = true;
                }
            }
            if(!isEmptyObject(o)){
                data['field']['list'].push(o);
            }
        }
        if(!(jie&&dai)){
            layer.alert(bt.lang('dataexchange.template.msg.both'), {icon:0});
            return false;
        }
        for(var x=0;x<trLD.length-1;x++){
            if(trLD[x+1]>trLD[x]){
                layer.alert(bt.lang('dataexchange.template.msg.order'), {icon:0});
                return false;
            }
        }

        var action = "/erp/swap/template/save";
        $.post(basePath + action,data.field,function(result){
            var error = "";
            if(!result.success && result.obj){
                var arr = result.obj.split(",");
                for(var i=0;i<arr.length;i++){
                    var e = bt.lang(arr[i]);
                    if(e){
                        if(error){
                            error+=","+e;
                        }else{
                            error+=e;
                        }
                    }
                }
            }else{
                error = result.msg;
            }
            parent.layer?parent.layer.alert(error, {icon: result.success?1:2}):layer.alert(error, {icon: result.success?1:2});
            if(result.success){
                if(bt.navtab) {
                    bt.navtab.refreshParent();
                    bt.navtab.closeCurrent();
                }else {
                    parent.layer ? parent.layer.closeAll() : layer.closeAll();
                }
            }
        },'JSON');
        return false;
    });
});
