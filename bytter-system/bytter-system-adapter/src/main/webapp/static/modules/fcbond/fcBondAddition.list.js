var datatable,dicts;
bt.use(['form','datatable','dateType','select','corp','money','moneyType','bank','btImport','upload','dicts'], function(){
    $ = bt.jquery,form = bt.form();
    bt.corp.init();
    bt.moneyType.init();  
    bt.dateType.init();
    dicts = bt.dicts.init();//字典
    bt.bank.init({
        elem:'.bank',
    });  
      
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        colReorder: true,
        checkbox:true,
        order: [[8,'asc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/bond/fcbondAddition/BondAdddatatable" 
        },
        buttons: [
            {
                hide:bt.isnotResource('netbank:fcBondAdd:deposit:apply'),
                text: bt.lang('common.button.add'),
                action: function ( e, dt, node, config ) {
                    add();
                }
            },{
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        fixedColumns: {
            rightColumns:1//代表冻结右边一列
        },
        columns: [
            {data:"billNo",bSortable:true,className:'center'},
            {data:"corpName",bSortable:true},
            {data:"bankName",bSortable:true},
            {data:"bankAcc",bSortable:true},
            {data:"accName",bSortable:true},
            {data:"curName",bSortable:true,className:'center'},
            {data:"bondMoney",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
            		return bt.money.format(full.bondMoney);
                }
            },
            {data:"addMoney",bSortable:true,className:'textRight',
            	render: function ( data, type, full, meta ) {
            		return bt.money.format(full.addMoney);
                }
            },
            {data:"createDate",bSortable:true,className:'center'},
           // {data:"isPayedName",bSortable:true,className:'center'},
            {data:"status",bSortable:true,className:'center',
                render: function (data, type, full, meta) {
                    return bt.dict("common.status",full.status);
                }
            },
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                var html ='';
                if(bt.isResource('netbank:fcBondAdd:deposit:apply',full.corpId) && (full.status!='11')){
                    html += '<a class="" href="#" data-row="'+meta.row+'" onclick="update(this)">'+bt.lang("common.button.edit")+'</a><span class="text-explode">|</span>';
                    html += '<a class="" href="#" data-row="'+meta.row+'" onclick="deleteColumn(this)">'+bt.lang("common.button.delete")+'</a><span class="text-explode">|</span>';
                }
                html += '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>';
            	html += ' \<span class="text-explode">|</span>\ <a class="" href="#" data-row="'+meta.row+'" onclick="listMsg(this)">'+bt.lang("common.button.logInfo")+'</a>';
                return html;
            }}
        ]
    });
});

//选择保证金账户进行追加
function add(){
    parent.layer.open({
        type: 2,
        area: ['850px', '560px'],
        maxmin: true,
        content: basePath + "/bond/fcbondAddition/selectBondAccPage"
    });
}

//查看追加保证金详情
function view(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/bond/fcbondAddition/viewBondAddition/" + data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bond/fcbondAddition/viewBondAddition/"+ data.id
    });
}

//编辑
function update(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.update.data'), basePath + "/bond/fcbondAddition/editBondAddition/" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/bond/fcbondAddition/editBondAddition/" + data.id,
            end: function () {
                datatable && datatable.draw();
            }
        });
    }
}

//删除行
function deleteColumn(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
        $.post(basePath + "/bond/fcbondAddition/deleteBondAddition/" + data.id, function(result){
            bt.datatable.refreshCurrentPage();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}

//日志列表
function listMsg(el){
	var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
	if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('common.button.logInfo'),basePath + "/bond/fcbondlog/listlog?bondId=" + data.id);
            return;
        }
	}
}
