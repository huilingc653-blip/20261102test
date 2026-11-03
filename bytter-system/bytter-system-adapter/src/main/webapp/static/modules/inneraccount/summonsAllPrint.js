bt.use(['form','moneyType','printType','layer'], function(){
    var moneyType = bt.moneyType.init();
});
var getAllowOnce=function(size){
    if(!size){
        size=1;
    }
    var allowOnceArr;
    bt.jquery.ajax({
        type: "GET",
        url: basePath + "/allowOnce?size="+size,
        dataType:"json",
        async: false,
        success: function(result){
            if(result){
                allowOnceArr = result;
            }
        }
    });
    return allowOnceArr;
};

    function prints() {
        var LODOP=getLodop();
        var allowOnce=getAllowOnce();
        $('input[name="fileName"]').each(function(i){
            var  fileName = $(this).val();
            var srcs = basePath + "/download/attachment?id=" + fileName + "&_allow_once_=" + allowOnce;
            $("#imger"+$(this).attr("alt")).attr("src", srcs);
        })

        document.getElementById('buttonDiv').style.display = 'none';
        LODOP.SET_PRINT_MODE("PRINT_PAGE_PERCENT","Auto-Width");
        LODOP.SET_PRINT_STYLEA(0, "FontName", "宋体");
        $('div[name="print_doc1"]').each(function(i){
            // printStyle 内容在oprate公共js中
            LODOP.ADD_PRINT_HTM(10,10,"100%","100%",printStyle+'<body>'+$(this).html()+'</body>');
            LODOP.NewPageA();
        })
        document.getElementById('buttonDiv').style.display = '';
        // LODOP.PREVIEW();
        if (LODOP.CVERSION){
            CLODOP.On_Return=function(TaskID,Value){
                if(Value>0){
                    layer.msg(bt.lang('common.message.print.success'));
                }else{
                    layer.msg(bt.lang('common.message.print.failure'));
                    return;
                }
            };
            LODOP.PREVIEW();
        }else{

            if(LODOP.PRINTA()){
                layer&&layer.msg(bt.lang('common.message.print.success'));
            }else{
                layer&&layer.msg(bt.lang('common.message.print.failure'));
                return;
            }
        }
    };

var closeForms = function () {
    parent.layer ? parent.layer.closeAll() : layer.closeAll();
};
