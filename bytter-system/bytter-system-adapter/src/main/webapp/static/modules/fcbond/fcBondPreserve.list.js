var datatable,dicts;
bt.use(['form','datatable','dateType','select','corp','money','moneyType','bank','dicts'], function(){
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
        order: [[11,'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/bond/fcbondpreserve/getDataTable"
        },
        buttons: [
            {
                hide:bt.isnotResource('netbank:fcBondPreserve:deposit:apply'),
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
            {data:null,bSortable:false,className:'center',//生成序号
                render: function (data, type, full, meta) {
                    var html ='';
                    html += '<lable class="" >'+(meta.row+1)+'</lable>'
                    return html;
                }},
            {data:"corpName",bSortable:true,className:'center'},
            {data:"itemNo",bSortable:true,className:'center'},
            {data:"startDate",bSortable:true,className:'center'},
            {data:"endDate",bSortable:true,className:'center'},
            {data:"bankAcc",bSortable:true,className:'center'},
            {data:"accName",bSortable:true,className:'center'},
            {data:"bankName",bSortable:true,className:'center'},
            {data:"curId",bSortable:true,className:'textRight',
            	render: function ( data, type, full, meta ) {
            		return full.curName;
                }},
            {data:"accBalance",bSortable:true,className:'textRight',
            	render: function ( data, type, full, meta ) {
            		return bt.money.format(full.accBalance);
                }},
            {data:"applyMoney",bSortable:true,className:'textRight',
            	render: function ( data, type, full, meta ) {
            		return bt.money.format(full.applyMoney);
                }},
            {data:"inputDate",
                render:function( data, type, full, meta){
                    return bt.date.format(full.inputDate, 'yyyy-mm-dd');
                },
                bSortable:true,className:'center'},
            {data:"status",bSortable:true,className:'center',
                render: function (data, type, full, meta) {
                    return bt.dict("fcBond.status",full.status);
                }
            },
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                var html ='';
                if(bt.isResource('netbank:fcBondPreserve:deposit:apply',full.corpId)&&(data.status=='0'||data.status=='-1')){
                    html += '<a class="" href="#" data-row="'+meta.row+'" onclick="update(this)">'+bt.lang("common.button.edit")+'</a><span class="text-explode">|</span>';
                    html += '<a class="" href="#" data-row="'+meta.row+'" onclick="deleteColumn(this)">'+bt.lang("common.button.delete")+'</a><span class="text-explode">|</span>';
                }
                if(bt.isResource('netbank:fcBondPreserve:deposit:query')){
                html += '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a><span class="text-explode">|</span>';
            	html += '<a class="" href="#" data-row="'+meta.row+'" onclick="listMsg(this)">'+bt.lang("common.button.logInfo")+'</a>';
                }
                if(bt.isResource('netbank:fcBondPreserve:deposit:apply',full.corpId)&&data.status=='95'&&(data.applyMoney!=0)){
                html += '<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="releaseAmount(this)">'+bt.lang("table.fcBond.release")+'</a>';
               }
                return html;
            }}
        ]
    });
});

//新增保证金项目
function add(){
  if (bt.navtab) {
	bt.navtab.addNavtab(bt.lang('common.button.add'), basePath + "/bond/fcbondpreserve/addBondItem");
	return;
   }
}

//查看保证金项目详情
function view(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bond/fcbondpreserve/viewBondItem/"+ data.id
    });
}

//编辑
function update(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.update.data'), basePath + "/bond/fcbondpreserve/editBondItem/" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/bond/fcbondpreserve/editBondItem/" + data.id,
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
        $.post(basePath + "/bond/fcbondpreserve/deleteBondItem/" + data.id, function(result){
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

//释放额度
function releaseAmount(el){
	 var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
	    parent.layer.confirm(bt.lang('common.fcBond.confirm.release'),function(){
	        $.post(basePath + "/bond/fcbondpreserve/releaseItemAmount/" + data.id, function(result){
	            bt.datatable.refreshCurrentPage();
	            parent.layer.alert(result.msg, {icon: result.success?1:2});
	        },'JSON');
	    });
}
