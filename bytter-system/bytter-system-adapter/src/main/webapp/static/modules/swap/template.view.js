bt.use(['form','select','element'], function(){
    var $ = bt.jquery,form = bt.form(),element = bt.element();
    var typeId = $("input[name='templateTypeId']").val();
    var templateId = $("input[name='id']").val();
    var openViewIndex = 0;//记录弹窗控件的总数
    //表头列表
    getTypeDtl(0,function(reslut){
        if(reslut){
            //获取表头数据
            getHeadData(reslut,function(data,map){
                var h_html = "",t_html = "";
                for(var i=0;i<data.length;i++){
                    var inputTypeCode = data[i].inputTypeCode;//输入方式
                    var code = data[i].fieldCode;
                    var value = getValue(code,map)||"";
                    h_html+='<th>'+data[i].showName+'</th>';
                    t_html+='<td class="center">';
                    if(inputTypeCode==='2'){//下拉框
                        t_html+='<div class="slectheadsource init" data-value="'+value+'" code="'+data[i].fieldCode+'" data-required="'+(data[i].notNull?true:false)+'" data-code="'+data[i].sourceCode+'"></div>';
                    }else if(inputTypeCode==='4'){//弹出框
                        t_html+= '<div class="layui-inline layui-disabled"><input type="text" readonly class="layui-input initInput layui-disabled"\
                                 data-value="'+value+'" data-code="'+data[i].sourceCode+'" id="inputType'+openViewIndex+'">\
                                <div class="bt-div-search"><i class="layui-icon">&#xe615;</i></div></div>';
                        openViewIndex++;
                    }else{
                        t_html+=value;
                    }
                    t_html+='</td>';
                }
                $("#head_thead").html('<tr>'+h_html+'</tr>');
                $("#head_table").html('<tr>'+t_html+'</tr>');
                //渲染
                initSource("slectheadsource",0);
                //初始化弹窗默认值
                initOpenView("initInput");
            });
        }
    });
    //凭证列表
    getTypeDtl(1,function(reslut){
        if(reslut){
            getProofData(reslut,function (data,list) {
                var h_html = '<th>'+bt.lang("table.fcAccBalance.balanceWay")+'</th>';
                for(var j=0;j<list.length;j++){
                    var map = list[j];
                    var loanDirection = list[j].loanDirection?1:0;
                    var t_html = "";
                    for(var i=0;i<data.length;i++){
                        var inputTypeCode = data[i].inputTypeCode;//输入方式
                        var code = data[i].fieldCode;
                        var value = getValue(code,map)||"";
                        if(j==0){
                            h_html+='<th>'+data[i].showName+'</th>';
                        }
                        t_html+='<td class="center">';

                        if(inputTypeCode==='2'){//下拉框
                            t_html+='<div class="slectsource initsel" data-value="'+value+'" code="'+data[i].fieldCode+'" data-required="'+(data[i].notNull?true:false)+'" data-code="'+data[i].sourceCode+'"></div>';
                        }else if(inputTypeCode==='4'){//弹出框
                            t_html+= '<div class="layui-inline layui-disabled"><input type="text" readonly class="layui-input initInput layui-disabled"\
                                 data-value="'+value+'" data-code="'+data[i].sourceCode+'" id="inputType'+openViewIndex+'">\
                                <div class="bt-div-search"><i class="layui-icon">&#xe615;</i></div></div>';
                            openViewIndex++;
                        }else{
                            t_html+=value;
                        }
                        t_html+='</td>';
                    }
                    if(j===0){
                        $("#botton_thead").html('<tr>'+h_html+'</tr>');
                    }
                    $("#botton_table").append('<tr class="yesdel"><td class="center">'+bt.dict("FcCompareAcc.loanSend",loanDirection)+'</td>'+t_html+'</tr>');
                }
                initSource("slectsource",1);
                $(".slectsource").removeClass("initsel");
                //初始化弹窗默认值
                initOpenView("initInput");
            });
        }
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
    function initSource(type,save){
        var sources = $("."+type);
        if(sources){
            for(var i=0;i<sources.length;i++){
                var code = $(sources[i]).data("code");
                var fcode = $(sources[i]).attr("code");
                var value = $(sources[i]).data("value")||"";
                bt.select.init({
                    elem: $(sources[i]),
                    name: fcode,
                    disabled: true,
                    defaultVal: value,
                    valueField: "code",
                    textField: "name",
                    initCallBack: function (elem,data) {
                    },
                    url: basePath+"/erp/swap/template/source?code="+code
                })
            }
        }
    }
    function getHeadData(data,fn) {
        $.ajax({
            type: "GET",
            url: basePath+"/erp/swap/template/json/"+templateId,
            dataType: 'json',
            success: function (result) {
                var map = {};
                if (result === null || !result) {
                    map = {};
                }else {
                    map = result;
                }
                if(fn){
                    fn(data,map);
                }
            }
        });
    }
    function getProofData(data,fn) {
        $.ajax({
            type: "GET",
            url: basePath+"/erp/swap/templatedtl/list/"+templateId,
            dataType: 'json',
            success: function (result) {
                var list = [];
                if (result === null || !result) {
                    list = [];
                }else {
                    list = result;
                }
                if(fn){
                    fn(data,list);
                }
            }
        });
    }
    function getValue(code,obj) {
        var value="";
        var c = code.replace(/_/g,"").toUpperCase();
        for(var key in obj){
            var temp = key.replace(/_/g,"").toUpperCase();
            if(c===temp){
                value = obj[key];
                break;
            }
        }
        return value;
    }
    //初始化每个弹窗控件
    function initOpenView(type){
        var sources = $("."+type);
        if(sources){
            for(var i=0;i<sources.length;i++){
                var sourceCode = $(sources[i]).data("code");
                var value = $(sources[i]).data("value")||"";
                var id = $(sources[i]).attr("id");
                //同步查询默认值
                $.ajax({
                    type:"POST",
                    url:basePath+"/erp/swap/template/getInputDate/"+value+"/"+sourceCode,
                    async:false,
                    dataType:'json',
                    success:function(result){
                        $("#"+id).val(result.obj);
                    }
                });
            }
        }
    }
});
