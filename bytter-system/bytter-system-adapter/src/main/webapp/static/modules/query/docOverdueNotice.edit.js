var datatable,moneyFun;
var rowTr = 0;
bt.use(['form', 'bttable', 'select', 'dateType', 'corp', 'money', 'moneyType', 'dicts', 'btImport', 'bank', 'selectType','element'], function () {
    $ = bt.jquery, form = bt.form();
    bt.corp.init();
    bt.moneyType.init();
    moneyFun = bt.moneyType.getFun();
    bt.dateType.init();
    bt.selectType.init();
    bt.dicts.init();

    var docId = $("#docId").val();

    $(document).on('click', ".delete-notic", function () {
        var tr = $(this).parents('tr');
        var index = tr.attr("row");
        //$('input[name="externalAcc[' + index + '].externalAcc"]').attr("value","");
        tr.remove();
        rowTr --;
        reindexRows();
    });

    function reindexRows(){
        $('table tbody tr').each(function(newIndex){
            $(this).find('input, select').each(function(){
                var name = $(this).attr('name');
                name = name.replace(/\[\d+\]/, '[' + newIndex + ']');
                $(this).attr('name',name);
            })
        })
    }


    form.on('submit(submit)', function(data) {
        $.post(basePath + '/accountReceivable/query/docOverdueNotice/save', data.field, function (result) {
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2},function () {
                layer.closeAll();
                if (result.success) {
                     parent.layer?parent.layer.closeAll():layer.closeAll();
                }
            });
        }, 'JSON');
        return false;
    });

     initCollectionTable =function (){
           $.ajax({
               url: basePath + "/accountReceivable/query/docOverdueNotice/datatableNotice?docId=" + docId,
               method: 'Post',
               async: false,
               success: function (result) {
                   //var data = JSON.parse(result);
                   initTable(result.data);
               }
           });
        }
    initCollectionTable();

});

var addRow = function(index){
    var html = '<tr row="' + index + '">';
    html += '<td style="text-align: center;display:none;"><input type="text" name="notices[' + index + '].id" autocomplete="off" class="layui-input layui-input-list"/></td>';
    html += '<td style="text-align: center;"><input type="text" lay-verify="required" name="notices[' + index + '].allocationDate" class="layui-input bt-input-inline bt-biz-dateType layui-input-list" data-type="date" /></td>';
    html += '<td style="text-align: center;"><input type="text" lay-verify="required|strlength" name="notices[' + index + '].allocationBy" autocomplete="off" class="layui-input layui-input-list" data-maxlength="10"/></td>';
    html += '<td style="text-align: center;"><input type="text" lay-verify="required|strlength" name="notices[' + index + '].allocationResult" autocomplete="off" class="layui-input layui-input-list" data-maxlength="30"/></td>';
    html += '<td style="text-align: center;"><input type="text" lay-verify="required|strlength" name="notices[' + index + '].overdueReason" autocomplete="off" class="layui-input layui-input-list" data-maxlength="30"/></td>';
    html += '<td style="text-align: center;"><a href="javascript:void(0);" class="add-notic">' + bt.lang("common.button.add") + '</a> &nbsp;&nbsp;'+ '<a href="javascript:void(0);" class="delete-notic">' + bt.lang("common.button.delete") + '</a></td>';
    html += '</tr>';
    var $row = $(html);
    $("#appTable tbody:last").append($row);
    form.render();
    bt.dateType.init($row.find('.bt-biz-dateType'));
    rowTr ++;
}

document.getElementById('appTable').addEventListener('click',function(event){
    if(event.target.classList.contains('add-notic')){
          var index = rowTr;
          addRow(index);
    }
})

function closecurrent(){
    parent.layer?parent.layer.closeAll():layer.closeAll();
    return false;
}

initTable = function (collectorList){
    if(collectorList.length == 0){
        addRow(0);
        return;
    }
    var html="";
    for( var rowIndex = 0; rowIndex < collectorList.length; rowIndex++ ){
        var collectorData = collectorList[rowIndex];
        html += '<tr data-row="' + rowIndex + '">';
        html += '<td style="text-align: center;">'+ '<input type="hidden" name="notices[' + rowIndex + '].id" value="'+collectorData.id+'"/>';
        html += '<input type="hidden" name="notices[' + rowIndex + '].createBy" value="'+collectorData.createBy+'"/>';
        html += '<input type="text" lay-verify="required" name="notices[' + rowIndex + '].allocationDate" value="'+collectorData.allocationDate+'" class="layui-input bt-input-inline bt-biz-dateType layui-input-list" data-type="date" /></td>';
        html += '<td style="text-align: center;"><input type="text" lay-verify="required|strlength" name="notices[' + rowIndex + '].allocationBy"  value="'+collectorData.allocationBy+'" autocomplete="off" class="layui-input layui-input-list" data-maxlength="10"/></td>';
        html += '<td style="text-align: center;"><input type="text" lay-verify="required|strlength" name="notices[' + rowIndex + '].allocationResult"  value="'+collectorData.allocationResult+'" autocomplete="off" class="layui-input layui-input-list" data-maxlength="30"/></td>';
        html += '<td style="text-align: center;"><input type="text" lay-verify="required|strlength" name="notices[' + rowIndex + '].overdueReason"  value="'+collectorData.overdueReason+'" autocomplete="off" class="layui-input layui-input-list" data-maxlength="30"/></td>';
        html += '<td style="text-align: center;"><a href="javascript:void(0);" class="add-notic">' + bt.lang("common.button.add") + '</a> &nbsp;&nbsp;';
        if(collectorData.editFlag == '1'){
            html += '<a href="javascript:void(0);" class="delete-notic">' + bt.lang("common.button.delete") + '</a></td>';
        }else{
            html += '<a href="javascript:void(0);" style="color: #d2d2d2;">' + bt.lang("common.button.delete") + '</a></td>';
        }

        html += '</tr>';
    }
    rowTr = collectorList.length;
    $("#appTable tbody:last").append(html);
    //$("#appTable").append(html);
    form.render();
    bt.dateType.init();
    return false;
}
