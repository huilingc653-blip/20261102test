var moneyType,moneyFun,dicts,form,dateFun;



bt.use(['form', 'datatable', 'dateType', 'corp', 'dicts', 'money', 'selectType', 'accountTable', 'bank', 'moneyType', 'mulselect', 'externalCorpBankAcc'], function () {
     var $ = bt.jquery; form = bt.form();
     moneyType = bt.moneyType.init();
     moneyFun = moneyType.getFun();
     dicts = bt.dicts.init();
     dateFun = bt.dateType.getFun();


    //读取并解析对象
    var temp = localStorage.getItem($('#docId').val());
    var autoData = JSON.parse(temp);
    //应收单数据
    $('.docManageVo-code').text(autoData.docManageVo.code?autoData.docManageVo.code:'-'); //单据编号
    $('.docManageVo-corpName').text(autoData.docManageVo.corpName?autoData.docManageVo.corpName:'-'); //单位
    $('.docManageVo-batchBusTypeName').text(autoData.docManageVo.batchBusTypeName?autoData.docManageVo.batchBusTypeName:'-'); //业务类型
    $('.docManageVo-sourceName').text(autoData.docManageVo.sourceName?autoData.docManageVo.sourceName:'-'); //单据来源
    $('.docManageVo-writeOffStatusName').text(autoData.docManageVo.writeOffStatusName?autoData.docManageVo.writeOffStatusName:'-'); //当前状态
    $('.afterDocWriteOffStatusName').text(autoData.afterDocWriteOffStatusName?autoData.afterDocWriteOffStatusName:'-'); //核销后状态
    $('.docManageVo-receiptDate').text(autoData.docManageVo.receiptDate?autoData.docManageVo.receiptDate:'-'); //单据日期
    $('.docManageVo-endDate').text(autoData.docManageVo.endDate?autoData.docManageVo.endDate:'-'); //到期日期
    $('.docManageVo-agingDate').text(autoData.docManageVo.agingDate?autoData.docManageVo.agingDate:'-'); //账龄起算日
    $('.docManageVo-receivableMoney').text(bt.money.format(autoData.docManageVo.receivableMoney)); //应收金额
    $('.docManageVo-writeOffBal').text(bt.money.format(autoData.docManageVo.writeOffBal)); //应收余额
    $('.docManageVo-executeMoney').text(bt.money.format(autoData.docManageVo.executeMoney)); //执行金额
    $('.docManageVo-newWriteOffBal').text(bt.money.format(autoData.docManageVo.newWriteOffBal)); //本次核销后应收余额
    $('.docManageVo-curName').text(autoData.docManageVo.curName?autoData.docManageVo.curName:'-'); //应收币种
    $('.docManageVo-voucherTypeName').text(autoData.docManageVo.voucherTypeName?autoData.docManageVo.voucherTypeName:'-'); //结算方式
    $('.docManageVo-externalName').text(autoData.docManageVo.externalName?autoData.docManageVo.externalName:'-'); //客商名称
    $('.docManageVo-externalTypeName').text(autoData.docManageVo.externalTypeName?autoData.docManageVo.externalTypeName:'-'); //客商类型
    $('.docManageVo-contractCode').text(autoData.docManageVo.contractCode?autoData.docManageVo.contractCode:'-'); //交易合同编号
    //匹配收入信息（流水数据）
    $('.bisAccDtlVO-bankSerialNo').text(autoData.bisAccDtlVO.bankSerialNo?autoData.bisAccDtlVO.bankSerialNo:'-'); //银行流水号
    $('.bisAccDtlVO-transTime').text(autoData.bisAccDtlVO.transTime?autoData.bisAccDtlVO.transTime:'-'); //交易日期
    $('.bisAccDtlVO-transDateTime').text(autoData.bisAccDtlVO.transDateTime?autoData.bisAccDtlVO.transDateTime:'-'); //流水时间
    $('.bisAccDtlVO-accName').text(autoData.bisAccDtlVO.accName?autoData.bisAccDtlVO.accName:"-"); //本方账户名
    $('.bisAccDtlVO-bankName').text(autoData.bisAccDtlVO.bankName?autoData.bisAccDtlVO.bankName:"-"); //本方账户银行名称
    $('.bisAccDtlVO-bankAcc').text(autoData.bisAccDtlVO.bankAcc?autoData.bisAccDtlVO.bankAcc:"-"); //本方账号
    $('.bisAccDtlVO-oppAccName').text(autoData.bisAccDtlVO.oppAccName?autoData.bisAccDtlVO.oppAccName:'-'); //对方账户名
    $('.bisAccDtlVO-oppAccNo').text(autoData.bisAccDtlVO.oppAccNo?autoData.bisAccDtlVO.oppAccNo:'-'); //对方账号
    $('.bisAccDtlVO-uses').text(autoData.bisAccDtlVO.uses?autoData.bisAccDtlVO.uses:'-'); //用途
    $('.bisAccDtlVO-amt').text(bt.money.format(autoData.bisAccDtlVO.amt)); //金额
    $('.bisAccDtlVO-writeOffBal').text(bt.money.format(autoData.bisAccDtlVO.writeOffBal)); //可核销金额
    $('.bisAccDtlVO-executeMoney').text(bt.money.format(autoData.bisAccDtlVO.executeMoney)); //执行金额
    $('.bisAccDtlVO-writeOffStatusName').text(autoData.bisAccDtlVO.writeOffStatusName?autoData.bisAccDtlVO.writeOffStatusName:'-'); //当前状态
    $('.afterRelationWriteOffStatusName').text(autoData.afterRelationWriteOffStatusName?autoData.afterRelationWriteOffStatusName:'-'); //核销后状态
    $('.bisAccDtlVO-postscript').text(autoData.bisAccDtlVO.postscript?autoData.bisAccDtlVO.postscript:'-'); //附言
    $('.bisAccDtlVO-curName').text(autoData.bisAccDtlVO.curName?autoData.bisAccDtlVO.curName:'-'); //币种
    //删除缓存数据
    localStorage.removeItem($('#docId').val());

});
