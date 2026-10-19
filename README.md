# Crawler
물감 데이터 확보를 위한 크롤러
<br/><br/>

### 크롤링 주의사항
***반드시*** 크롤링이 가능한 페이지인지 확인해야 한다.
크롤링 가능 여부는 사이트명 뒤에 `/robots.txt`을 붙여 확인한다. <br/>
ex) `https://oldholland.com/robots.txt`, `https://www.goldenpaints.com/robots.txt`, ...

<br/><br/>
## 목차
1. [환경설정](#1-환경설정)
2. [실행방법](#2-실행방법)  
   2.1. [크롤링](#21-크롤링)  
   2.2. [판다스](#22-판다스)  
   2.3. [이미지 크롤링](#23-이미지-크롤링)
<br/><br/>

## 1. 환경설정
- 아나콘다 (참고: https://annajang.tistory.com/26)
- 주피터 노트북
<br/>

<br/>
아나콘다 사용 시 크롤링에 필요한 여러 라이브러리를 설치할 필요가 없어 편리하다. 
<br/><br/>

## 2. 실행방법
### 2.1. 크롤링
크롤링은 BeautifulSoup를 사용해 진행한다. 
1. bs4와 request를 import한다
   ```
   import bs4
   import requests
   ```
   <br/>
2. request 라이브러리 사용해 크롤링하고자 하는 페이지의 status code를 가져온다
   ```
   html = request.get(url) 
   ```
   url에는 크롤링하고자 하는 페이지의 링크가 들어간다.(ex.https://www.oldholland.com/acrylics/)
   <br/><br/>

4. BeautifulSoup를 사용해 가져온 코드를 lxml구조로 변환한다
   ```
   soup = bs4.BeautifulSoup(html.text, 'lxml')
   ```
   html.text를 통해 가져온 HTML문서를 lxml 파서를 통해 BeautifulSoup 객체로 변환한다.<br/>
   +) [lxml](https://hi-guten-tag.tistory.com/7) 이란?
   <br/><br/>
5. 원하는 태그를 find, select 등의 메소드를 활용해 가져온다<br/>
   - find(name, attrs, recursive, string, **kwargs)    
     스캔할 대상에서 제일 처음 발견되는 요소 하나를 반환한다
     ```
     metadata = soup.find('div', class_='wrapper')
     ```
   - select(.css property)  
     스캔할 대상에서 발견되는 모든 요소를 배열로 반환한다.  
     find태그와 다르게 문자열 형식으로 파라미터를 입력한다.  
     - <code>metadata = soup.select('div')</code> : soup 내 모든 div 태그를 가져온다  
     - <code>metadata = soup.select('div.main-content')</code> : soup 내 main-content라는 클래스를 가진 모든 div태그를 가져온다  
     - <code>metadata = soup.select('div#wrapper1')</code> : soup내 wrapper1이라는 id를 가진 모든 div 태그를 가져온다
     - <code>metadata = soup.select('div a')</code> : soup 내 있는 a태그들 중 상위 태그 중 div태그가 있는 모든 태그들만 가져온다
     - <code>metadata = soup.select('div>a')</code> : div의 바로 아래 있는 자식 태그들 중 a태그를 가져온다
    <br/>
    이외에도 find_all(), select_one(), find_parents(), children, find_next_siblings() 등의 메소드를 적절히 활용하여 원하는 요를 가져올 수 있다.  
<br/>
참고: https://www.crummy.com/software/BeautifulSoup/bs4/doc/  
<br/><br/>

### 2.2. 판다스
판다스를 활용하여 사전, 배열 등의 자료형을 DataFrame으로 만들고, 이를 xlsx 파일에 저장하자  
1. 엑셀 파일 열기
   ```
   writer = pd.ExcelWriter('엑셀파일명.xlsx', engine='xlsxwriter')
   ```  
   ExcelWriter 메소드를 사용하면 존재하는 파일명이라면 해당 파일을 열고, 없는 파일이라면 해당 위치에 파일을 만든다
   <br/><br/>
2. 저장할 데이터 DataFrame으로 변환
   ```
   df = DataFrame(변환할 자료)
   ```
   <br/><br/>
3. 원하는 시트에 데이터 저장
   ```
   df.to_excel(writer, sheet_name="시트이름")
   ```
   <br/><br/>
4. 엑셀 파일 저장
   ```
   writer.save()
   ```
 <br/>
시트에 원하는 데이터를 저장한 후에는 반드시 writer.save()를 통해 엑셀 파일을 저장해야 한다.<br/>
참고: https://m.blog.naver.com/PostView.naver?isHttpsRedirect=true&blogId=kiddwannabe&logNo=221597578686 <br/>
https://seong6496.tistory.com/109
<br/><br/>

### 2.3. 이미지 크롤링
urllib 라이브러리를 사용하여 이미지를 크롤링한다
```
import bs4
import requests
from urllib.request import urlopen

testUrl = 'https://www.oldholland.com/wp-content/uploads/2018/01/A1-oil-tube-400x600.jpg'

with urlopen(testUrl) as f:
     with open('./img.jpg','wb') as h:
            img = f.read()
            h.write(img)
```
참고: https://bskyvision.com/entry/Wind10Anacondapython-%EC%9B%B9%EC%97%90%EC%84%9C-%EC%9D%B4%EB%AF%B8%EC%A7%80-%EC%88%98%EC%A7%91%ED%95%98%EA%B8%B0-%EC%9D%B4%EB%AF%B8%EC%A7%80-%ED%81%AC%EB%A1%A4%EB%A7%81
