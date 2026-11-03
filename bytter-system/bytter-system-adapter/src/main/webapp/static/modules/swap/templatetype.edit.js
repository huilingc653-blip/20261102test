bt.use(['form','select','element'], function(){
    var $ = bt.jquery,form = bt.form(),element = bt.element();
    var inputT = [];//存储每行输入方式的控件的sel对象
    var selectFiled = bt.select.init({
        elem:'.selectFiled',
        name:'matchingField',
        required: true,
        valueField: "fieldCode",
        textField:"fieldName",
        placeholder:{value:'',text:'请选择'},
        url: basePath+"/erp/swap/templatedatadefine/json?flag=0",
        change:function(data,text){
            $("#head_table").find("input[type='checkbox']").removeAttr("disabled");
            var $tr = $("#head_table").find("tr[code='head_"+data.value+"']");
            $tr.find("input.isNotNull").prop("checked",true).prop("value",true).attr("disabled",true);
            $tr.find("input.isShow").prop("checked",true).prop("value",true).attr("disabled",true);
            $tr.find("input.showName").prop("disabled","");
            form.render();
        }
    });
    var dataAll = getTplData(typeId,[],[]);
    //遍历表头字段
    dealhtml("head_table",dataAll.hdata,0);
    var sv = $(".selectFiled").data("defaultvalue");
    if(sv){
        var $tr = $("#head_table").find("tr[code='head_"+sv+"']");
        $tr.find("input.isNotNull").attr("disabled",true);
        $tr.find("input.isShow").attr("disabled",true);
        form.render();
    }
    //遍历凭证
    dealhtml("botton_table",dataAll.bdata,dataAll.hdata.length||0);
    //渲染遍历数据来源
    var sddiv = $(".selectSource");
    if(sddiv && sddiv.length>0){
        var sourceData = getSourceData(1,[])||[];
        for(var i=0;i<sddiv.length;i++){
            var index = $(sddiv[i]).attr("index");
            var defaulv = $(sddiv[i]).data("value")||"";
            bt.select.init({
                elem: $(sddiv[i]),
                name: 'typeDtlList['+index+'].sourceCode',
                valueField: "code",
                textField: "name",
                defaultVal: defaulv,
                placeholder:{value:'',text:'请选择'},
                data: sourceData,
                change:function(data,text){
                    var index1 = $(data.othis).parents("tr").data("row");
                    if(data.value===" ")
                    //设置默认“文本框”
                        inputT[index1].setValue('1');
                    else
                    //设置默认“下拉框”
                        inputT[index1].setValue('2');
                    form.render();
                }
            });
        }
    }
    //渲染遍历输入方式
    var itdiv = $(".selectInputType");
    if(itdiv && itdiv.length>0){
        var inputType = getInputType(1,[])||[];
        for(var i=0;i<itdiv.length;i++){
            var defaulv = $(itdiv[i]).data("value")||"";
            var index = $(itdiv[i]).attr("index");
            var sel = bt.select.init({
                elem: $(itdiv[i]),
                name: 'typeDtlList['+index+'].inputTypeCode',
                valueField: "code",
                textField: "name",
                search:true,
                defaultVal:defaulv,
                data: inputType
            });
            inputT[index] = sel;//保存每个sel对象
        }
    }

    form.render('checkbox');
    //监听点击事件
    form.on("checkbox(isshow)",function(data){
        data.elem.value = data.elem.checked;
        if(data.elem.checked){
            $(data.elem).parent("td").parent("tr").find("input.showName").prop("disabled","");
        }else {
            if($(data.elem).parent("td").parent("tr").find("input.isNotNull").prop("checked")){
                $(data.elem).parent("td").parent("tr").find("input.isNotNull").prop("checked",false);
                $(data.elem).parent("td").parent("tr").find("input.isNotNull").prop("value",false);
                /*if($(data.elem).parents("tbody#head_table").length>0){
                    selectFiled.setValue("");
                }*/
            }
            $(data.elem).parent("td").parent("tr").find("input.showName").prop("disabled","disabled");
        }
        form.render();
    });
    form.on("checkbox(isnotnull)",function(data){
        data.elem.value = data.elem.checked;
        if(data.elem.checked){
            if(!$(data.elem).parent("td").parent("tr").find("input.isShow").prop("checked")){
                $(data.elem).parent("td").parent("tr").find("input.isShow").prop("checked",true);
                $(data.elem).parent("td").parent("tr").find("input.isShow").prop("value",true);
                $(data.elem).parent("td").parent("tr").find("input.showName").prop("disabled","");
            }
            /*if($(data.elem).parents("tbody#head_table").length>0){
                var code = $(data.elem).parent("td").parent("tr").attr("code");
                if(code){
                    selectFiled.setValue(code.replace("head_",""));
                }
            }*/
        }else{
            /*if($(data.elem).parents("tbody#head_table").length>0){
                selectFiled.setValue("");
            }*/
        }
        form.render();
    });
    //监听提交
    form.on('submit(submit)', function(data){
        var action = "/erp/swap/templatetype/modify";
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
    
     //监听暂存
    form.on('submit(save)', function(data){
        //1、根据匹配字段【预算科目】,【模版头字段设置】列表中的【预算科目】必须为[显示]并且[必录]
        // 2、校验凭证 显示或者必录两者必须有一个勾选 (必录是必填的意思)

        var action = "/erp/swap/templatetype/tempModify";
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
                parent.layer?parent.layer.alert(error, {icon: result.success?1:2}):layer.alert(error, {icon: result.success?1:2});
                if(result.success){
                    if(bt.navtab) {
                        bt.navtab.refreshParent();
                        bt.navtab.closeCurrent();
                    }else {
                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                    }
                }
            }
        },'JSON');
        return false;
    });
    
    function getTplData(id,data,dataB) {
        $.ajax({
            type: "GET",
            url: basePath+"/erp/swap/templatetypedtl/json/"+id,
            dataType: 'json',
            async: false,
            success: function (result) {
                if (result === null || !result) {
                    data = [];
                    dataB = [];
                }else {
                    for(var i=0;i<result.length;i++){
                        if(result[i].isTemplateDtl==0){
                            data.push(result[i]);
                        }
                        if(result[i].isTemplateDtl==1){
                            dataB.push(result[i]);
                        }
                    }
                }
            }
        });
        return {
            hdata: data,
            bdata: dataB
        };
    }
    //组合html
    function dealhtml(domId,data,index) {
        if($("#"+domId).length<1 || data.length<1){
            return;
        }
        var html = "";
        for(var i=0;i<data.length;i++){
            html+= '<tr code="head_'+data[i].fieldCode+'" data-row="'+(i+index)+'"><td class="center">'+data[i].fieldName+'</td>\
                <input type="hidden" name="typeDtlList['+(i+index)+'].id" value="'+data[i].id+'"><input type="hidden" name="typeDtlList['+(i+index)+'].typeId" value="'+data[i].typeId+'">\
                <input type="hidden" name="typeDtlList['+(i+index)+'].fieldCode" value="'+data[i].fieldCode+'"><input type="hidden" name="typeDtlList['+(i+index)+'].fieldName" value="'+data[i].fieldName+'">\
                <input type="hidden" name="typeDtlList['+(i+index)+'].templateDtl" value="'+data[i].isTemplateDtl+'">\
                <td class="center"><input type="checkbox" '+(data[i].isShow?'checked':'')+' name="typeDtlList['+(i+index)+'].show" value="'+(data[i].isShow?true:false)+'" class="isShow" lay-skin="primary" lay-filter="isshow"></td>\
                <td class="center"><input type="checkbox" '+(data[i].isNotNull?'checked':'')+' name="typeDtlList['+(i+index)+'].notNull" value="'+(data[i].isNotNull?true:false)+'" class="isNotNull" lay-skin="primary" lay-filter="isnotnull"></td>\
                <td class="center">\
                <input type="text" name="typeDtlList['+(i+index)+'].showName" '+(data[i].isShow?'':'disabled')+' value="'+data[i].showName+'" autocomplete="off" lay-verify="strlength" data-maxlength="100" class="layui-input showName">\
                </td>\
                <td class="center"><div class="selectSource" index="'+(i+index)+'" data-value="'+(data[i].sourceCode?data[i].sourceCode:'')+'"></div></td>\
                \<td class="center"><div class="selectInputType" index="'+(i+index)+'" data-value="'+(data[i].inputTypeCode?data[i].inputTypeCode:'')+'"></div></td></tr>';
        }
        $("#"+domId).html(html);
    }
    function getSourceData(flag,data) {
        $.ajax({
            type: "GET",
            url: basePath+"/erp/swap/templatetype/json?flag="+flag,
            dataType: 'json',
            async: false,
            success: function (result) {
                if (result === null || !result) {
                    data = [];
                }else {
                    data = result;
                }
            }
        });
        return data;
    }
    function getInputType(flag,data) {
        $.ajax({
            type: "GET",
            url: basePath+"/sys/dict/listByGroupCode?code=trans.inputType",
            dataType: 'json',
            async: false,
            success: function (result) {
                if (result === null || !result) {
                    data = [];
                }else {
                    data = result;
                }
            }
        });
        return data;
    }
});
