bt.use(['form', 'moneyType', 'printType'], function () {
    bt.moneyType.init();
//            bt.printType.init();
});
var getAllowOnce = function (size) {
    if (!size) {
        size = 1;
    }
    var allowOnceArr;
    bt.jquery.ajax({
        type: "GET",
        url: basePath + "/allowOnce?size=" + size,
        dataType: "json",
        async: false,
        success: function (result) {
            if (result) {
                allowOnceArr = result;
            }
        }
    });
    return allowOnceArr;
};

function prints() {
    var LODOP = getLodop();
    var fileName = $("#fileName").val();
    var allowOnce = getAllowOnce();
    var imgSrc = basePath + "/download/attachment?id=" + fileName + "&_allow_once_=" + allowOnce;

    // 先加载图片
    var image = new Image();
    image.crossOrigin = "Anonymous"; // 如果有跨域问题
    image.src = imgSrc;

    image.onload = function() {
        var base64 = imgToBase64(image);
        $("#imger").attr("src", base64);

        // 图片加载完成后再执行打印
        performPrint(LODOP);
    };

    image.onerror = function() {
        console.error("图片加载失败");
        layer.msg("签章图片加载失败");
        // 可以选择是否继续打印
        performPrint(LODOP);
    };
}

function performPrint(LODOP) {
    document.getElementById('buttonDiv').style.display = 'none';
    var printHtmlPre = "<!DOCTYPE html>\n" +
        "<html lang=\"en\">\n" +
        "<head>";
    printHtmlPre += $('head').html();
    printHtmlPre += "</head>";

    LODOP.ADD_PRINT_HTM(0, 0, "100%", "100%",
        printHtmlPre + printStyle + '<body>' + $("#print_doc1").html() + '</body>');

    // 继续原有的打印逻辑...
    if (LODOP.CVERSION) {
        CLODOP.On_Return = function (TaskID, Value) {
            if (Value > 0) {
                printSuccess();
            } else {
                layer.msg(bt.lang('common.message.print.failure'));
                return;
            }
        };
        LODOP.PREVIEW();
    } else {
        if (LODOP.PRINTA()) {
            printSuccess();
        } else {
            layer.msg(bt.lang('common.message.print.failure'));
            return;
        }
    }
}
 // 使用HTMLCanvasElement将图像转换为base64
    function imgToBase64(img) {
        // 创建一个canvas元素
        var canvas = document.createElement('canvas');
        var ctx = canvas.getContext('2d');

        // 设置canvas尺寸与图片尺寸相同
        canvas.width = img.width;
        canvas.height = img.height;

        // 将图片绘制到canvas上
        ctx.drawImage(img, 0, 0);

        // 返回图像的base64表示
        return canvas.toDataURL();
    }


function printSuccess() {
    var param = {
        corpId: $("#corpId").val(),
        billNo: $("#billNo").val(),
        date: (new Date()).getTime()
    };
    var url = basePath + "/receipt/billPrint/prints";
    $.ajax({
        url: url, method: 'POST', data: param, async: false, dataType: 'json',
        success: function (data) {
            if (data && data.success) {
                layer.msg(bt.lang('common.message.print.success'));
            }
        }
    });
}


function getStatusValue(ValueType, ValueIndex, oResultOB) {
    LODOP = getLodop();
    if (LODOP.CVERSION) {
        LODOP.On_Return = function (TaskID, Value) {
            oResultOB = Value;
        };
    }
    var strResult = LODOP.GET_VALUE(ValueType, ValueIndex);
    if (!LODOP.CVERSION) {
        return strResult;
    } else {
        return "";
    }
};


var closeForms = function () {
    parent.layer ? parent.layer.closeAll() : layer.closeAll();
};
