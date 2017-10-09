package com.minlia.module.bible.v1.service;

import com.minlia.module.bible.v1.domain.Bible;
import com.minlia.module.bible.v1.domain.BibleItem;
import com.minlia.module.bible.v1.repository.BibleItemRepository;
import com.minlia.module.bible.v1.repository.BibleRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by will on 8/27/17.
 */
@Service
public class BibleInitializeServiceImpl implements BibleInitializeService {

  @Autowired
  BibleRepository bibleRepository;


  @Autowired
  BibleItemRepository bibleItemRepository;

  /**
   * 初始化系统Bible配置项
   * 当有的时候不需要插入, 不存在时插入
   */
  public void initialBibleWithCode(String bibleCode, String bibleItemCode, String bibleItemLabel,
      String bibleItemNotes,String targetBusinessCode) {
    Bible bibleFound = bibleRepository.findOneByCode(bibleCode);
    //不为空, 已找到此BIBLE
    if (null != bibleFound) {
      //查找是否有子项, 没有则初始化
      initialBibleItem(bibleItemCode, bibleItemLabel, bibleItemNotes, bibleFound);
    } else {
      //没找到此BIBLE先创建
      Bible entity = new Bible();
      entity.setCode(bibleCode);
      entity.setNotes("系统自动初始化配置项" + bibleCode);
      entity.setNotes(bibleCode);
      if(!StringUtils.isEmpty(targetBusinessCode)){
        entity.setTargetBusinessCode(targetBusinessCode);
      }
      Bible entityCreated = bibleRepository.save(entity);
      initialBibleItem(bibleItemCode, bibleItemLabel, bibleItemNotes, entityCreated);
    }
  }


  public void initialBibleItem(String bibleItemCode, String bibleItemLabel, String bibleItemNotes,
      Bible bible) {
    BibleItem bibleItemFound = bibleItemRepository
        .findOneByBible_IdAndCode(bible.getId(), bibleItemCode);

    if (null == bibleItemFound) {
      BibleItem bibleItem = new BibleItem();
      bibleItem.setCode(bibleItemCode);
      bibleItem.setLabel(bibleItemLabel);
      bibleItem.setNotes(bibleItemNotes);
      if (null != bible) {
        bibleItem.setBible(bible);
      }
      bibleItemRepository.save(bibleItem);
    }
  }

}