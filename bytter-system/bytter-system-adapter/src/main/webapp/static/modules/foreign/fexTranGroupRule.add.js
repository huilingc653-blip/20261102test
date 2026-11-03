var rowNum = 0;

bt.use(['form', 'jquery','select','selectType', 'bttable', 'dicts','selectTableType'], function () {
    /**初始化单选框begin，组件:select */
    var form = bt.form(), $ = bt.jquery;
    /**初始化单选框end，组件:select */

    /**按钮事件begin*/
    form.on('submit(submitEvent)', function(data) {
        var action = "/foreign/fextrangrouprule/save";
        $.post(basePath + action, data.field, function (result) {
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}, function () {
                parent.layer.closeAll();
                if (result.success) {
                    if (bt.navtab) {
                        bt.navtab.closeCurrent();
                        bt.navtab.refreshParent();
                    } else {
                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                    }
                }
            })
        }, 'JSON');
        return false;
    });
    /**按钮事件end*/

    var selectType = bt.selectType.init();
    /**初始化表格begin*/
    bt.bttable.init({
        scrollX:false,
        scrollY:false,
        elem: "#ruleDtl",
        url: basePath + "/foreign/fextrangrouprule/detail/0",
        fixedColumns: {
            rightColumns:1
        },
        columns:[
        ],
        delRowCallback: function (data, $tr) {
            $tr.remove();
        },
        // tableInitCallback: function (data) {
        //     bt.dicts.init();
        //     bt.selectType.init();
        // }
    });
    $(document).on('click', ".delete-data", function () {
        var tr = $(this).parents('tr');
        var index = tr.attr("row");
        $('input[name="fexTranGroupRuleDtlVOList[' + index + '].tranType"]').attr("value","");
        $('input[name="fexTranGroupRuleDtlVOList[' + index + '].proId"]').attr("value","");
        tr.remove();
    });

    $("#add").click(function(){
        var html = '<tr row="' + rowNum + '">';
        html += '<td style="text-align: center;"><div class="bt-biz-dict" data-required="true" data-callback="changePro" data-type="select" data-group="fexCorp.tranType"  data-name ="fexTranGroupRuleDtlVOList[' + rowNum + '].tranType"  data-displayData="7,8,9,2,3,4"></div></td>';
        html += '<td style="text-align: center;"><div class="bt-biz-selectType" data-required="true" data-biz-name="proId' + (rowNum+1)  + '"data-name ="fexTranGroupRuleDtlVOList[' + rowNum + '].proId" data-valueField="id" data-textField="proName"></div></td>';
        html += '<td style="text-align: center;"><a href="javascript:void(0);" class="delete-data">' + bt.lang("common.button.delete") + '</a></td>';
        html += '</tr>';
        $("#ruleDtl").append(html);
        bt.selectType.init();
        bt.dicts.init();
        form.render();
        rowNum ++;
    });
    /**初始化表格end*/

    /**产品回调begin*/
    changePro = function(data){
        var str = $(data.elem).attr('name');
        str = str.replace('fexTranGroupRuleDtlVOList[','');
        str = str.replace('].tranType','');
        var num = parseInt(str)+1;
        var url = '';
        if (data.value == '9'){ //查询全部的产品
            url = basePath+"/foreign/fexproduct/listProduct/"+" ";
        } else if (bt.empty(data.value)){
            url = basePath+"/foreign/fexproduct/listProduct/-1";
        } else {
            url = basePath+"/foreign/fexproduct/listProduct/" + data.value;
        }
        selectType.get('proId'+num).attr({"data-url":url,"data-defaultValue":""});
        form.render();
    };
    //重新渲染回调
    bt.dicts.init();
    /**产品回调end*/
});

/**
 * 判断字符串是否为空
 * @param ms
 * @returns {boolean}
 */
bt.empty = function(ms){
    if(typeof(ms) == "undefined" || ms == null || ms == "" || ms == " "){
        return true;
    }
    return false;
};
