import {Cookies} from 'react-cookie';

const cookies = new Cookies();

export const setCookie = (name, value, options) => {

	const defaultOptions = {
		path: '/',
		secure: true, // HTTPS를 사용할 경우 true로 설정
		sameSite: 'None' // 크로스 사이트 요청을 허용하려면 None으로 설정
	  };
	const finalOptions = { ...defaultOptions, ...options };

 	return cookies.set(name, value, finalOptions); 
}

export const getCookie = (name) => {
 return cookies.get(name); 
}

export const removeCookie = (name) => {
	cookies.remove(name, { path: '/', secure: true, sameSite: 'None' });
};