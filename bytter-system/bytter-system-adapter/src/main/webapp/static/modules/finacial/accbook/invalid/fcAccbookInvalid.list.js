bt.use(['form','datatable','btdate','moneynum', 'money', 'moneyType', 'dateType','corp','bank','selectType'], function(){
    $ = bt.jquery,form = bt.form();
    bt.selectType.init();
    bt.dateType.init();
    bt.corp.init();
    bt.moneyType.init();
    bt.bank.init({
   	   elem:'.bank',
    });

    //格式化数字
    $.each($("input[decimal]"),function(i,item){
        if($(item).attr("decimal")!=""){
            bt.moneynum.init({elem:$(item),decimal:2});
        }
    });
    //时间
    $.each($("div[type='date']"),function(i,item){
        bt.btdate.init({
            elem: "[field='"+$(item).attr("field")+"']",
            name: $(item).attr("field"),
            start: $(item).attr("defaultVal"),
            format: "YYYY-MM-DD",
            istime: false
        });
    });

    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },

        checkbox:true,order: [1,'desc'],
        ajax: {
            'url': basePath+"/finacial/accbook/fcaccbookinvalid/datatable"
        },
        buttons: [
            {
                text: bt.lang('批量作废'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    batchInvalid(data);
                }
            }
        ]
        ,columns: [
            {data:"busRaceId",bSortable:true,className:'center',render: function (data, type, full, meta) {
			    return full.raceName;
			}},
            {data:"voucherType",bSortable:true,className:'center',render: function (data, type, full, meta) {
			    return full.voucherName;
			}},
            {data:"tallyDate",bSortable:true},
            {data:"interestStartDate",bSortable:true},
            {data:"orderNo",bSortable:true},
            {data:"money",bSortable:false,className:'textRight',render: function (data, type, full, meta) {
            	return bt.money.format(data);
            }},
            {data:"rmk",bSortable:false},
            {data:"voucherState",bSortable:false,render: function (data, type, full, meta) {
                return bt.dict("Finacial.VoucherState", data);
            }},
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                var html = '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>\
                <a class="" href="#" data-row="'+meta.row+'" onclick="add(this)">'+bt.lang("Finacial.accbook.invalid.button.add")+'</a>';
                return html;
            }}
        ]
    });
});
//作废前校验凭证的付款指令状态判断
function add(el){
	var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
	var bookId = data.id;
	var action = "/finacial/accbook/fcaccbookinvalid/invalidBefore?bookId="+bookId;
    $.post(basePath + action, bookId,function(result){
        if(isEmpty && !isEmpty(result)){
        	if(result == "0"){
        		invalidAdd(el);
        	} else if (result == "1"){
        		var raceName = data.raceName || '';
        		layer.alert(raceName + bt.lang("fin.book.invalid.notallow"), {icon: 2});
        	} else if (result == "2"){
        		layer.alert(bt.lang("fin.book.invalid.bisexc.normal"), {icon: 2});
        	}
        }
    },'JSON');
    return false;
}

function invalidAdd(el){
	var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('Finacial.accbook.invalid.button.add'),basePath + "/finacial/accbook/fcaccbookinvalid/add?id=" + data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['1000px', '600px'],
        maxmin: true,
        content: basePath + "/finacial/accbook/fcaccbookblned/add?id=" + data.id,
        end:function(){
        	bt.navtab.refreshCurrentTable();
        }
    });
}



function view(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['1000px', '600px'],
        maxmin: true,
        content: basePath + "/finacial/accbook/fcaccbook/view?id=" + data.id
    });
}
function update(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('common.update.data'),basePath + "/accbook/fcaccbookblned/update?id=" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['1000px', '600px'],
            maxmin: true,
            content: basePath + "/finacial/accbook/fcaccbookblned/update?id=" + data.id,
            end:function(){
            	bt.navtab.refreshCurrentTable();
            }
        });
    }
}
function deleteColumn(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
        $.post(basePath + "/finacial/accbook/fcaccbookblned/delete/" + data.id, function(result){
        	bt.navtab.refreshCurrentTable();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}


//批量作废
function batchInvalid(data) {
    var len = data.length;
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var ids = "";
    var rowData = data.splice(0, len);

    for (var i = 0; i < rowData.length; i++) {
        var result = validFcAccBook(rowData[i]);//作废前校验
        if (result == "0") {
            if (ids == "") {
                ids += rowData[i].busGlobalId;;
            } else {
                ids += "," + rowData[i].busGlobalId;;
            }
        } else if (result == "1") {
            layer.alert("流水号：" + rowData[i].orderNo + bt.lang("fin.book.invalid.notallow"), {icon: 2});
            return;
        } else if (result == "2") {
            layer.alert("流水号：" + rowData[i].orderNo + bt.lang("fin.book.invalid.bisexc.normal"), {icon: 2});
            return;
        }
    }

    parent.layer.confirm(bt.lang('确认作废？'),function(){
        parent.layer.prompt({
            title: bt.lang('作废原因'),
            formType: 2
        },function(backMsg) {
            $.post(basePath + "/finacial/accbook/fcaccbookinvalid/batchInvalid", {ids: ids, backMsg: backMsg}, function (result) {
                datatable && datatable.draw();
                parent.layer?parent.layer.closeAll():layer.closeAll();
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
            }, 'JSON');
        });
    });

}

//作废前校验
function validFcAccBook(data) {
    var result = "0";
    $.ajax({
        url: basePath + "/finacial/accbook/fcaccbookinvalid/invalidBefore",
        method: 'POST',
        async: false,
        data: {bookId: data.id},
        dataType: 'json',
        success: function (data) {
            if (data) {
                result = data;
            }
        }
    });
    return result;
}