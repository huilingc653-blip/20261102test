var corp,dicts,moneyType,dateType,up,billTable;
bt.use(['form','datatable','moneyType','corp','select','dicts','dateType','btImport'], function(){
    $ = bt.jquery,form = bt.form();
    corp=bt.corp.init();
    dicts=bt.dicts.init();
    moneyType=bt.moneyType.init();
    dateType=bt.dateType.init();

    //生成列表
    billTable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox:true,order: [[1,'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/fundwarn/btwarnstrategy/datatable"
        },
        buttons: [
            {
                text: bt.lang('common.button.add'),
                hide:bt.isnotResource('fundwarn:warnstrategy:op'),
                action: function ( e, dt, node, config ) {
                    add();
                }
            }
        ]
        ,columns: [
            {data:"netName",bSortable:true,className:'center'},
            {data:"code",bSortable:true,className:'center'},
            {data:"name",bSortable:true,className:'center'},
            {data:"typeCodeName",bSortable:true,className:'center',
                render: function ( data, type, full, meta ) {
                    return bt.dict("FUNDWARN_TYPE_CODE",full.typeCode);
                }
            },
            {data:"curName",bSortable:true,className:'center'},
            {data:"dimensionCodeName",bSortable:true,className:'center',
                render: function ( data, type, full, meta ) {
                    return bt.dict("FUNDWARN_DIMENSION_CODE",full.dimensionCode);
                }
            },
            {data:"warnLevel",bSortable:true,className:'center',
                render: function ( data, type, full, meta ) {
                    return bt.dict("btwarnstrategy.level",full.warnLevel);
                }
            },
            {data:"statusName",bSortable:true,className:'center',
                render: function ( data, type, full, meta ) {
                    return bt.dict("FUNDWARN_STATUS",full.status);
                }
            },
            {data:"createDate",bSortable:true,className:'center'},
            {data:"updateDate",bSortable:true,className:'center'},
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                var html=''
                //if(bt.isResource('fundwarn:warnstrategy:op',full.netId)){
                    if(full.typeCode!='eqCorpPay' && full.typeCode!='bankAccPay' && full.typeCode!='cashPay' && full.typeCode!='daySumPay' ){
                        html='<a class="" href="#" data-row="'+meta.row+'" onclick="scanParameter(this)">'+bt.lang("common.button.warnstrategy.scanparameter")+'</a>';
                        html+='<span class="text-explode">|</span>';
                    }
                    html+='<a class="" href="#" data-row="'+meta.row+'" onclick="update(this)">'+bt.lang("common.button.edit")+'</a>';
                    html+='<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="copy(this)">'+bt.lang("common.button.clone")+'</a>';
                //}
                return html;
            }}
        ],
        fixedColumns:{
        	rightColumns:1
        }
    });

//币别
    bt.select.init({
        elem: '.currency',
        name: 'eqs_curId',
        valueField: 'id',
        textField: 'curName',
        search: true,
        disabled: false,
        url: basePath + '/basic/business/btcurrency/json'
    });
});

    
function add(){
	if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.add'),basePath + "/fundwarn/btwarnstrategy/add");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/fundwarn/btwarnstrategy/add",
        end:function(){
//            datatable && datatable.draw();
			bt.datatable.refreshCurrentPage();
        }
    });
}

function scanParameter(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.warnstrategy.scanparameter') ,
        		basePath + "/fundwarn/btwarnstrategy/scanParameter?warnId="+data.id);
        return;
    }
   layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/fundwarn/btwarnstrategy/scanParameter?warnId="+data.id
    });
}

function update(el){
	var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
	if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.edit'),basePath + "/fundwarn/btwarnstrategy/updateOrCopy?warnId="+data.id+"&opType=update");
        return;
    }
    if(data){
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/fundwarn/btwarnstrategy/updateOrCopy?warnId="+data.id+"&opType=update",
            end:function(){
//                datatable && datatable.draw();
    			bt.datatable.refreshCurrentPage();
            }
        });
    }
}

function copy(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.clone'),basePath + "/fundwarn/btwarnstrategy/updateOrCopy?warnId="+data.id+"&opType=copy");
        return;
    }
    if(data){
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/fundwarn/btwarnstrategy/updateOrCopy?warnId="+data.id+"&opType=copy",
            end:function(){
//                datatable && datatable.draw();
                bt.datatable.refreshCurrentPage();
            }
        });
    }
}
