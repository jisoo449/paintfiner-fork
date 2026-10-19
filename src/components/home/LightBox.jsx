import styles from "./LightBox.module.css";

import Lightbox from "yet-another-react-lightbox";
import Counter from "yet-another-react-lightbox/plugins/counter";
import Inline from "yet-another-react-lightbox/plugins/inline";

import "yet-another-react-lightbox/styles.css";
import "yet-another-react-lightbox/plugins/thumbnails.css";
import "yet-another-react-lightbox/plugins/counter.css";

/**
 * 이미지를 라이트박스 형식으로 보여주는 컴포넌트
 * 
 * @param {*} index 프론트 기준 선택된 이미지 순번 
 * @param {*} setIndex 인덱스 세터
 * @param {*} slides 화면에 보일 이미지 배열
 * @param {*} title 이미지 이름
 * @param {*} artist 이미지 작가
 *  
 * @returns 커다란 이미지
 */
export default function LightBox({index, setIndex, slides, title, artist}){

    // 큰 사진 화면 너비
    const inline = {
        style: {
            width: "100%",
            height: "100%",
            aspectRatio: "5 / 2",
            margin: "0 auto",
        },
    };

    // 큰 이미지 CSS
    const carousel = { 
        spacing: 0,
        padding: 0,
        imageFit: "contain"
    }

    // 카운터 CSS
    const counter = {
        container: { 
            style: { 
                top: "unset", 
                bottom: 0 
            }
        }
    }

    return (

        <div className={styles.wrapper_lightbox}>

            <Lightbox
                index={index}
                on={{ view: ({ index: currentIndex }) => setIndex(currentIndex) }}
                slides={slides}
                plugins={[Inline, Counter]}
                inline={inline}
                carousel={carousel}
            />

            <div className={styles.art_artist}>
                <p><b>{title}</b> by {artist}</p>
            </div>

        </div>

    )
}