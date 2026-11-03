bt.use(['form','element'], function(){
    var $ = bt.jquery;
    var dataAll = getTplData(typeId,[],[]);
    dealhtml("head_table",dataAll.hdata);
    dealhtml("botton_table",dataAll.bdata);
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
    function dealhtml(domId,data) {
        if($("#"+domId).length<1 || data.length<1){
            return;
        }
        var html = "";
        for(var i=0;i<data.length;i++){
            html+= '<tr><td class="center">'+data[i].fieldName+'</td>\
                <td class="center">'+data[i].showStr+'</td>\
                <td class="center">'+data[i].notNullStr+'</td>\
                <td class="center">'+(data[i].showName?data[i].showName:'')+'</td>\
                <td class="center">'+(data[i].sourceName?data[i].sourceName:'')+'</td>\
                <td class="center">'+(data[i].inputTypeName?data[i].inputTypeName:'')+'</td></tr>';
        }
        $("#"+domId).html(html);
    }
});
