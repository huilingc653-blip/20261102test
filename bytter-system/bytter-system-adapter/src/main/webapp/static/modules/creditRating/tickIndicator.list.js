var intreeTable,$;
bt.use(['jquery','treeTable','form','dicts'], function() {
    $ = bt.jquery, form = bt.form();
    bt.dicts.init();
        intreeTable= bt.treeTable.init({elem: "#scoringIndicatorTable", column: 2});

        // 科目父子联动
        $(".checkIds").on('change',function () {
            var checked = $(this).is(":checked");
            var cid = $(this).attr("id");
            var pid = $(this).data("parentid");
            setParent(pid,checked)
            //选中父科目子科目选中
            setChild(cid,checked);
            // setCheckAllButton()

            // 如果是未级节点时，需要提醒
            if($(this).closest('tr').hasClass('leaf') && !checked && pid){
                // 如果所有子都没有选中，将会提醒
                var allSamePidCheckboxs = $('#scoringIndicatorTable').find('.checkIds[data-parentid="'+pid+'"]');
                var unCheckedAllLeaf = false;
                bt.each(allSamePidCheckboxs,function () {
                    if(this.checked){
                        unCheckedAllLeaf = this.checked;
                        return
                    }
                })
                if(!unCheckedAllLeaf){
                    parent.layer.msg(getTopParentTypeName(cid)+' 下存在未勾选的最低层级指标，该类指标评价模板中不支持展示')
                }
            }
        })

        function getTopParentTypeName(id) {
            var pid = $('[data-tt-id="'+id+'"]').data('tt-parent-id');
            if(pid){
               return  getTopParentTypeName(pid)
            }else{
                return $('[data-tt-id="'+id+'"]').find('.type-name').text() || ''
            }
        }

        function setChild(cid,checked){
            if(cid){
                // 子选中
                var childrenCheckboxs = $('[data-parentid="'+cid+'"]');
                childrenCheckboxs[0] && childrenCheckboxs.prop("checked",checked);

                // 递归子孙选中
                childrenCheckboxs.each(function (i,item) {
                    setChild($(item).attr('id'),checked)
                })
            }
        }

        function setParent(pid,checked){
            if(pid){
                // 父节点选中
                $('#'+pid).prop("checked",true);
            }
            // 祖先级选中
            var parentsCheckboxs = $('[id="'+pid+'"]');
            if(parentsCheckboxs[0]){
                parentsCheckboxs.each(function (i,item) {
                    setParent($(item).data('parentid'),checked)
                })
            }
        }

    //全选
    $('#checkallcorp').on('change',function () {
        $("#scoringIndicatorTable").find(".checkIds").prop("checked", $(this).is(":checked"))
    })
// form.on('checkbox(checkallcorp)', function (data) {
//     var checked = data.elem.checked;
//     var checkboxs = $("#scoringIndicatorTable").children("tbody").find("input[type='checkbox']");
//     bt.each(checkboxs, function () {
//         $(this).prop("checked", checked)
//     })
//     form.render("checkbox")
// });

})

/**
 * 重置
 */
function resetDate() {
    $("#scoringIndicatorCode").val("");
    $("#scoringIndicatorName").val("");
}

/**
 * 将数据回写给前一个页面
 */
var submitData = function(){
    //获取勾选数据
    var checkboxs = $("#scoringIndicatorTable").children("tbody").find("input[type='checkbox']");
    var checkedIds="";
    bt.each(checkboxs, function () {
        if($(this).prop("checked")){
            if(checkedIds==""){
                checkedIds=$(this).attr("id");
            }else {
                checkedIds=checkedIds+","+$(this).attr("id");
            }
        }
    })
    var parentWindow = bt.navtab.getCurrentDom();
    parentWindow.checkBack(checkedIds);
    parent.layer.closeAll();
}



function expandAll() {
    //展开
    $("#scoringIndicatorTable").treetable('expandAll');
}

function collapseAll() {
    //收起
     $("#scoringIndicatorTable").treetable('collapseAll');

}
