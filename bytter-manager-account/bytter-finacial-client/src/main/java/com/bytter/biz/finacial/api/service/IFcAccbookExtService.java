package com.bytter.biz.finacial.api.service;
import com.bytter.biz.finacial.api.dto.FcAccbookExtDTO;
/**
 * @author YonJar
 * @date 2023/4/23.
 * @description
 */
public interface IFcAccbookExtService {
    FcAccbookExtDTO getBookExtVOByBookId(String bookId);
}
