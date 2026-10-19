import React, { useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom';
import ProductUnitList from '../product/ProductUnitList';
import styles from './ListSection.module.css'
import { Link } from 'react-router-dom';

function useQuery() {
  const { search } = useLocation();
  return React.useMemo(() => new URLSearchParams(search), [search]);
}

export default function ListSection({paints}) {
  const query = useQuery();
  const [keyword, setKeyword] = useState(null);
  const [category, setCategory] = useState(null);
  const [subCategory, setSubCategory] = useState(null);
  const [subsubCategory, setSubsubCategory] = useState(null);
  
  useEffect(()=>{
    setCategory(query.get('category'));
    setSubCategory(query.get('subcategory'));
    setSubsubCategory(query.get('subsubcategory'));
  }, [query]);

  return (
    <div className={styles.wrapper}>
      {category &&(
      <div className={styles.list_cate}>
        {category && <Link className={styles.cate_list} to={`/search?category=${category}`}><p className={styles.p_cate}>{category}</p></Link>}
        {subCategory && <Link className={styles.cate_list} to={`/search?category=${category}&subcategory=${subCategory}`}><p className={styles.p_cate}>/{subCategory}</p></Link>}
        {subsubCategory && <Link className={styles.cate_list} to={`/search?category=${category}&subcategory=${subCategory}&subsubcategory=${subsubCategory}`}><p className={styles.p_cate}>/{subsubCategory}</p></Link>}
      </div>)}
      <ProductUnitList paints={paints} page={"Search"}/>
    </div>
  );
}