import React,{ useState} from 'react';
import { categoriesKor } from '../../constants/categoriesKor.constants.js';
import { cateKey } from '../../constants/cateKey.constants.js';
import styles from './Navbar.module.css'
import { Link } from 'react-router-dom';

/**
 * 상품 카테고리 선택 navigation bar
 * @returns 
 */
export default function Navbar() { 

  const [categoryName, setCategoryName] = useState(null);                 // 물감 대분류(유화, 아크릴, etc). 검색에 사용
  const [subCategoryName, setSubCategoryName] = useState(null);           // 물감 분류(전통유화, 수성유화, etc). 검색에 사용

  return (
    <nav onMouseLeave={() => setCategoryName(null)}>

      <ul className={styles.category_ul}>

        {// change to 이 부분은 추후 삭제하고 밑에 부분을 살리면 된다.
        // cate1을 네비게이션 바에 표시
          categoriesKor[0]["subcategory"].map((category, idx) => (
          <li 
            className={styles.category_li}
            key={`category_${category.cate}`}
            onMouseEnter={() => setCategoryName(category.cate)}
          >
            <Link 
              className={styles.category_link} 
              to={`/paint/${cateKey[category.cate]}`}
            >
              {category.cate}
            </Link>

            {categoryName && category.cate===categoryName && categoryName!="푸어링 기법" && (
              <div className={styles.will_deleted} key={`category_${category.cate}`}>
                
                {category.category.map((subCategory, idx)=>(           
                        <li
                          className={styles.subCategory_li}
                          key={`category_${categoryName}|subcategory_${subCategory.cate}`}
                          onMouseEnter={() => setSubCategoryName(subCategory.cate)}
                        >
                          <Link className={styles.subCategory_link} to={`/paint/${cateKey[categoryName]}/${cateKey[subCategoryName]}`}>
                            {subCategory.cate}
                          </Link>
                        </li>
                ))}
              
              </div>
            )}

          </li>
        ))}
      </ul>

      {/* <ul className={styles.category_ul}>
        {// 대분류 카테고리: 물감, 붓, 서페이스, 스튜디오
         categoriesKor.map((category, idx) => (
          <li
            className={styles.category_li}
            key={`category_${category.cate}`}
            onMouseEnter={() => setCategoryName(category.cate)}
          >
            <Link className={styles.category_link} to={`/paint/${cateKey[category.cate]}`} >{category.cate}</Link>
          </li>
        ))}
      </ul> */}
      {/* {categoryName && (
        <div className={styles.dropdown_container} onMouseLeave={() => setSubCategoryName(null)}>

          {/* 대분류 반복문 */}
          {categoriesKor.map((category, idx) => (
            <React.Fragment key={`category_${category.cate}`}>
              {// 1차 카테고리: 유화, 수채화, 과슈, etc
               category.cate === categoryName && (
                <div>
                  <ul className={styles.subCategory_ul}>

                    {/* category 반복 */}
                    {category.subcategory.map((subCategory, idx) => (
                      <li
                        className={styles.subCategory_li}
                        key={`category_${categoryName}|subcategory_${subCategory.cate}`}
                        onMouseEnter={() => setSubCategoryName(subCategory.cate)}
                      >
                        <Link className={styles.subCategory_link} to={`/search/${cateKey[categoryName]}?cate1=${cateKey[subCategoryName]}`}>
                          {subCategory.cate}
                        </Link>
                      </li>
                    ))}
                  </ul>
                  {// 2차 카테고리: 전통 유화, 수성 유화, etc
                   category.subcategory.map((subCategory) => (
                    <div key={`category_${categoryName}|subcategory_${subCategory.cate}`}>
                      {subCategory.cate === subCategoryName && (
                        <ul className={styles.subsubCategory_ul}>

                          {/* subcategory.category.cate 반복 */}
                          {subCategory.category.map((subsubCategory, idx) => (
                            <li
                              className={styles.subsubCategory_li}
                              key={`category_${categoryName}|subcategory_${subCategoryName}|subsubcategory_${subsubCategory.cate}`}
                            >
                              <Link value={subsubCategory.cate} className={styles.subsubCategory_link} to={`/search/${cateKey[categoryName]}?cate1=${cateKey[subCategoryName]}&cate2=${cateKey[subsubCategory.cate]}`}>
                                {subsubCategory.cate}
                              </Link>
                            </li>
                          ))}
                        </ul>
                      )}
                    </div>
                  ))}
                </div>
              )}
            </React.Fragment>
          ))}
    </nav>
  );
}