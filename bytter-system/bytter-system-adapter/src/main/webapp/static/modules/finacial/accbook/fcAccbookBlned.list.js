bt.use(['form','datatable','btdate','moneynum','dateType','corp','bank','moneyType'], function(){
    $ = bt.jquery,form = bt.form();
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

        checkbox:false,order: [[2,'desc']],
        ajax: {
            'url': basePath+"/finacial/accbook/fcaccbookblned/datatable"
        },
        buttons: [
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
            {data:"rmk",bSortable:false},
            {data:"money",bSortable:false,className:'textRight',render: function (data, type, full, meta) {
			    return bt.money.format(data);
			}},
            {data:"voucherState",bSortable:false,render: function (data, type, full, meta) {
                return bt.dict("Finacial.VoucherState", data);
            }},
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                var html = '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>\
                <a class="" href="#" data-row="'+meta.row+'" onclick="add(this)">'+bt.lang("Finacial.accbook.rush.button.add")+'</a>';
                return html;
            }}
        ]
    });
});
function add(el){
	var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
	var bookId = data.id;
	var action = "/finacial/accbook/fcaccbookblned/rushBefore?bookId="+bookId;
    $.post(basePath + action, bookId,function(result){
        if(isEmpty && !isEmpty(result)){
        	if(result == "0"){
        		rushAdd(el);
        	} else if (result == "1"){
        		var raceName = data.raceName;
        		layer.alert(raceName + bt.lang("fin.book.rush.notallow"), {icon: 2});
        	} else if (result == "2"){
        		layer.alert(bt.lang("fin.book.rush.bisexc.normal"), {icon: 2});
        	}
        }
    },'JSON');
    return false;
}

function rushAdd(el){
	var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('Finacial.accbook.rush.button.add'),basePath + "/finacial/accbook/fcaccbookblned/add?id=" + data.id);
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
        area: ['1120px', '800px'],
        maxmin: true,
        content: basePath + "/finacial/accbook/fcaccbookblned/view?id=" + data.id,
        end:function(){
        	bt.navtab.refreshCurrentTable();
        }
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
