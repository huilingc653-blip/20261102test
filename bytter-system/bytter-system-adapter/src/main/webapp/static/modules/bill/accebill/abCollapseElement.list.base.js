bt.use(['form','select','datatable','element'], function(){
	$ = bt.jquery,form = bt.form();
	var element = bt.element();
    element.on('collapse(collapse)', function(data){
        if($.fn.dataTable){
            $.fn.dataTable.tables({visible: true, api: true}).columns.adjust();
        }
    });
});
