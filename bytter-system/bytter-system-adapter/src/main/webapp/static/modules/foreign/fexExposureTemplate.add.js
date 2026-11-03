bt.use(['form', 'layedit', 'selectType', 'dicts','treeTable','moneyType'], function () {
    var $ = bt.jquery, form = bt.form();
    bt.treeTable.init({elem:"#itemTable",column:1});
    //总填报周期单位
    bt.totalFillUnitCallback = function (data, $elem){
        var totalFillUnit = bt.dicts.get('totalFillUnit').val();
        if (totalFillUnit == "1") {
            bt.dicts.get('isYearFill').attr({'data-disabled': false});
        } else {
            bt.dicts.get('isYearFill').val(0);
            bt.dicts.get('isYearFill').attr({'data-disabled': true});
        }
        validPeriod();
    }
    function validPeriod() {
        //总填报周期
        var totalFillPeriod = moneyType.get('totalFillPeriod').val();
        var totalFillUnit = bt.dicts.get('totalFillUnit').val();
        //滚动总周期
        var scrollFillPeriod = moneyType.get('scrollFillPeriod').val();
        var scrollFillUnit = bt.dicts.get('scrollFillUnit').val();
        //滚动分解子周期
        var scrollResolvePeriod = moneyType.get('scrollResolvePeriod').val();
        var scrollResolveUnit = bt.dicts.get('scrollResolveUnit').val();

        var totalFillMonths = 0;
        var scrollFillMonths = 0;
        var scrollResolveMonths = 0;
        if (totalFillPeriod && totalFillUnit && totalFillUnit.trim() != "" && scrollFillPeriod && scrollFillUnit && scrollFillUnit.trim() != "") {
            //1.校验滚动总周期是否超过总填报周期
            if (totalFillUnit == "1") {
                totalFillMonths = moneyFun.mul(totalFillPeriod, 12);
            } else if (totalFillUnit == "2") {
                totalFillMonths = moneyFun.mul(totalFillPeriod, 4);
            } else {
                totalFillMonths = totalFillPeriod;
            }

            if (scrollFillUnit == "1") {
                scrollFillMonths = moneyFun.mul(scrollFillPeriod, 12);
            } else if (scrollFillUnit == "2") {
                scrollFillMonths = moneyFun.mul(scrollFillPeriod, 4);
            } else {
                scrollFillMonths = scrollFillPeriod;
            }

            if (scrollFillMonths > totalFillMonths) {
                layer.alert(bt.lang("foreign.message.scrollFillPeriod.gt.totalFillPeriod"), {icon: 2});
                return false;
            }

            //2.校验滚动分解子周期是否超过滚动总周期
            if (scrollResolvePeriod && scrollResolveUnit && scrollResolveUnit.trim() != "") {
                if (scrollResolveUnit == "1") {
                    scrollResolveMonths = moneyFun.mul(scrollResolvePeriod, 12);
                } else if (scrollResolveUnit == "2") {
                    scrollResolveMonths = moneyFun.mul(scrollResolvePeriod, 4);
                } else {
                    scrollResolveMonths = scrollResolvePeriod;
                }
                if (scrollResolveMonths > scrollFillMonths) {
                    layer.alert(bt.lang("foreign.message.scrollResolvePeriod.gt.scrollFillPeriod"), {icon: 2});
                    return false;
                }
            }
        }
        return true;
    }
    //填报周期校验
    bt.periodCallback = function (data, $elem){
        validPeriod();
    }
    var moneyType = bt.moneyType.init();
    var moneyFun = moneyType.getFun();
    bt.dicts.init();
    form.on("checkbox(itemCheck)",function(data){
        //子科目的checkbox选中时，全部被选中或不选中
        var checked = data.elem.checked;
        var cid = $(data.elem).parents("tr").data("tt-id");
        var pid = $(data.elem).parents("tr").data("tt-parent-id");
        setParent(pid,checked)
        //选中父科目子科目选中
        setChild(cid,checked);
        //设置已选科目
        setCheckItemInfo();
    })

    function setCheckItemInfo() {
        var itemNames = "";
        $('form').find('input[name="itemCodes"]:checked').each(function(){
            if (itemNames == "") {
                itemNames = $('#itemName' + $(this).val()).val() + '(' + $(this).val() + ")";
            } else {
                itemNames += '、'+$('#itemName' + $(this).val()).val() + '(' + $(this).val() + ")";
            }
        });
        $("#checkItemInfo").text(itemNames);
    }

    function setChild(cid,checked){
        if(cid){
            var childTr =  $("table tr").find('tr[data-tt-parent-id="'+cid+'"]');
            childTr.each(function(data,dom){
                var childId = $(dom).data("tt-id");
                if(childId){
                    $(childTr).find("input").prop("checked",checked);
                    form.render("checkbox");
                }
                var childChildTr = $("table tr").find('tr[data-tt-parent-id="'+childId+'"]');
                if(childChildTr.length !==0){
                    setChild(childId,checked)
                }
            })
        }
    }

    function setParent(pid,checked){
        if(pid){
            var childTrs = $("table").find('tr[data-tt-parent-id="'+pid+'"]');
            $('table tr[data-tt-id="'+pid+'"]').find("input").prop("checked",true)
            form.render("checkbox");
        }
        var id = $("table").find('tr[data-tt-id="'+pid+'"]').data("tt-parent-id");
        var parentTr = $("table").find('tr[data-tt-id="'+id+'"]')
        if(parentTr.length !==0){
            setParent(id,checked)
        }
    }

    //点击全选按钮
    $("#checkAll").on("click",function(){
        if($(this).hasClass("all")){
            $(this).removeClass("all");
            $(this).text(bt.lang('common.check.unAll'))
            $("table").find('input[lay-filter="itemCheck"]').prop("checked",true);
        }else{
            $(this).addClass("all");
            $(this).text(bt.lang('common.check.all'))
            $("table").find('input[lay-filter="itemCheck"]').prop("checked",false);
        }
        //设置已选科目
        setCheckItemInfo();
        form.render("checkbox")
    })

    //监听提交
    form.on('submit(submit)', function (data) {
        if(!validPeriod()){
            return false;
        }
        var itemCodes = [];
        var itemIds = [];
        $('form').find('input[name="itemCodes"]:checked').each(function(){
            itemCodes.push($(this).val());
            itemIds.push($('#itemId'+$(this).val()).val());
        });
        if(itemCodes.length > 0){
            data.field.itemCodes = itemCodes;
            data.field.itemIds = itemIds;
        }else{
            layer.alert(bt.lang("table.btwarnstrategy.subjectName.errorMessage"), {icon: 2});
            return false;
        }
        var action = "/foreign/fexExposureTemplate/save";
        $.post(basePath + action, data.field, function (result) {
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}, function () {
                parent.layer.closeAll();
                if (result.success) {
                    if (bt.navtab) {
                        bt.navtab.refreshParent();
                        bt.navtab.refreshCurrent()
                    } else {
                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                    }
                }
            })
        }, 'JSON');
        return false;
    });
});
