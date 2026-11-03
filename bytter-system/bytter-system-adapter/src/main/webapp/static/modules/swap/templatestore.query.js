var form,configType,erpDatatable;
bt.use(['form','element','datatable','selectType','dateType','bank','corp','moneyType','dicts'], function(){
    form = bt.form(),$  = bt.jquery,element = bt.element();
    configType = $("input[name='dealType']").val();
    bt.dicts.init();
    bt.moneyType.init();
    bt.dateType.init();
    bt.selectType.init();
    bt.corp.init();
    bt.bank.init({elem: '.bank'});
    loadDataTable();
});

var getDataField=function(){
    var action = "/erp/swap/templatestore/dynaDataHeader";
    var dataField={};
    bt.jquery.ajax({
        type: "GET",
        url: basePath + action,
        data: {"type":configType},
        dataType:"json",
        async: false,
        contentType : 'application/json;charset=utf-8', //设置请求头信息
        success: function(result){
            dataField = result;
        },
        error: function(res){
        }
    });
    return dataField;
};
var getDataColumns=function(dataField){
    var dataColumn=new Array();
    if(dataField&&dataField.length>0){
        for(i=0;i<dataField.length;i++){
            var field={};
            // if("status" == dataField[i].field){
            //     field={data:dataField[i].field,title:bt.lang(dataField[i].text),bSortable:true,className:'textRight',
            //         render: function ( data, type, full, meta ) {
            //             return full.statusN;
            //         }
            //     }
            // }else
                if("num"==dataField[i].type){
                field={data:dataField[i].field,title:bt.lang(dataField[i].text),bSortable:true,className:'textRight',
                    render: function ( data, type, full, meta ) {
                        return bt.money.format(data,{separator: "",decimal: 0});
                    }
                }
            }else if("money"==dataField[i].type){
                field={data:dataField[i].field,title:bt.lang(dataField[i].text),bSortable:true,className:'textRight',
                    render: function ( data, type, full, meta ) {
                        return bt.money.format(data);
                    }
                }
            }else if("dynaSource"==dataField[i].type){
                field={data:dataField[i].field,title:bt.lang(dataField[i].text),bSortable:true,className:'textRight',
                    render: function ( data, type, full, meta ) {
                        var colIndex = meta.col;
                        try{
                            var name =  meta.settings.aoColumns[colIndex].name;
                            var gCode = bt.dynaDataSource(configType,name);
                            return bt.dynaSource(gCode, data);
                        }catch (e) {
                            return ''
                        }
                    }
                };
            }else if("action"==dataField[i].type){
                field={data:null,bSortable:false,title:bt.lang(dataField[i].text),className:'center',render: function (data, type, full, meta) {
                        return '<a class="" href="javascript:;" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.dtl.view")+'</a>';
                }}
            }else{
                field={data:dataField[i].field,title:bt.lang(dataField[i].text),bSortable:true};
            }
            dataColumn[i]=field;
        }
    }
    return dataColumn;
};
var loadDataTable=function(){
    var dataField=getDataField();
    var dataColumns=getDataColumns(dataField);
    //生成列表
    if(erpDatatable){
        $('#appTable').DataTable().destroy();
        $('#appTable').html('');
    }
    erpDatatable = bt.datatable.init({
        elem: "#appTable",
        colReorder: true,
        checkbox: true,
        order: [[1, 'desc']],
        paging: true,
        fixedColumns: {
            leftColumns:1
        },
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/erp/swap/templatestore/datatable/query"
        },
        buttons: [
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        columns:dataColumns,
        summary: {
            pageSum: false,
            allSum: true,
            selectSum: true,
            sumTextCol:2,
            format: {
                decimal: 2
            },
            sumList: [
                {
                    colNum: '9',
                    dataName: 'amt'
                }
            ]
        },
        // 暂时注释导出全部回调
        customExportAll: function (params, processing, data) {
            var exportUrl = basePath + "/erp/swap/templatestore/exportAllQuery";
            openExportExcel(exportUrl, data);
            processing(false);
        }
    });
};

//查看明细
function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();

    var newSet = $("#newSet").val();
    var url=basePath + "/erp/swap/hisdtltemplate/view/" + data.dtlId+"?type="+configType;
    if(newSet == 1){
        url= basePath + "/erp/swap/hisDtlComField/view/" + data.dtlId+"?type="+configType;
    }

    var perContent = layer.open({
        type: 2,
        area: ['800px', '600px'],
        maxmin: true,
        content: url
    });
    layer.full(perContent);
}