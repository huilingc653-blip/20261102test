layui.use(['form','jquery','treeTable','checkboxs'], function(){
    $ = layui.jquery;form = bt.form();
    bt.treeTable.init({elem:"#corp-table",column:0});
    /**
     * 全选
     */
    form.on('checkbox(checkAllCollectCorp)', function (data) {
        var checked = data.elem.checked;
        $("#corp-table").children("tbody").find("input[data-name='checkCollect']").filter(":not([disabled])").prop("checked", checked);
        $("#corp-table").children("tbody").find("input[data-name='checkCollectAll']").filter(":not([disabled])").prop("checked", checked);
        form.render("checkbox");
    });

    /**
     * 选择控制
     */
    form.on("checkbox(checkCollectCorp)", function (data) {
        //控件右边的checkbox选中时，全部被选中或不选中
        var cid = $(data.elem).data("collect-child-id");
        var checked = data.elem.checked;
        getControlCorp(cid, checked);
        //上边全选时，左边相应的要被全选中或不全不选中
        // var pid = $(data.elem).data("collect-parent-id");
        // getControlNet(cid);
        form.render("checkbox")
    });

    /**
     * 设置控制网点是否选中
     * @param cid
     */
    function getControlNet(pid) {
        if (pid) {
            var checkedboxsLen = $("#corp-table").find("input[data-collect-parent-id ='" + pid + "']:checked").length;
            var checkboxsLen = $("#corp-table").find("input[data-collect-parent-id ='" + pid + "']").length;

            var checkboxschild = $("#corp-table ").find("input[data-collect-child-id ='" + pid + "']");
            bt.each(checkboxschild, function () {
                if(checkedboxsLen === checkboxsLen){
                    $(this).prop("checked", true);
                }else {
                    $(this).prop("checked", false);
                }
                var pidchild = $(this).data("collect-parent-id");
                if (pidchild) {
                    getControlNet(pidchild);
                }
            });
        }
    };

    /**
     * 设置单位是否选中
     * @param cid
     */
    function getControlCorp(cid, checked) {
        // $("#corp-table").find("input[data-collect-parent-id ='" + cid + "']").filter(":not([disabled])").prop("checked", checked);
        $("#corp-table").find("input[data-collect-parent-id ='" + cid + "'][name='checkCollect']").filter(":not([disabled])").prop("checked", checked);
        var cidparent = $("#corp-table").find("input[data-collect-parent-id ='" + cid + "']").data("collect-child-id");
        if (cidparent && cidparent.length) {
            getControlCorp(cidparent, checked)
        }
    }

    //点击传送数据
    transferData=function(data) {
        var corpMaps=[];
        $('form').find('input[data-name="checkCollect"]:checked').each(function () {
            var data = {
                id:$(this).data("collect-id"),
                corpId:$(this).val(),
                corpName:$(this).data("collect-name"),
                corpCode:$(this).data("collect-code"),
                corpParentId:$(this).data("collect-parent-id"),
                corpType:$(this).data("collect-corptype"),
                corpNetId:$(this).data("collect-netid")
            };

            corpMaps.push(data);
        });

        if(corpMaps.length > 0){
            var data=corpMaps;
        }else{
            corpMaps.push("");
            var data=corpMaps;
        }
        var currentParentIframe = top.$('.layui-tab-content > .layui-show').find('iframe')[0]['name'];
        var func = top.frames[currentParentIframe].window.btSelectCallBack['bt-biz-mulCorp']['callback'];
        if (func) {
            var data =data;
            func(data);
        }

    };

    //重置
    resetCode=function(){
        $("#corp-table").children("tbody").find("input[data-name='checkCollect']").filter(":not([disabled])").prop("checked", false);
        $("#corp-table").children("tbody").find("input[data-name='checkCollectAll']").filter(":not([disabled])").prop("checked", false);
        $("#corp-table").children("tbody").find("input[data-name='checkAllCollectCorp']").filter(":not([disabled])").prop("checked",false);
        form.render("checkbox");
    }
});