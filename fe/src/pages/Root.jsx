import React, {useEffect} from 'react';
import { Outlet } from 'react-router-dom';
import Header from '../components/common/Header';
import Navbar from '../components/common/Navbar';
import Navigation from '../components/common/Navigation';
import Footer from '../components/common/Footer';
import CompareList from '../components/CompareList';
import { useRecoilValue } from "recoil";
import { compareState } from "../states/compareAtom";

export default function Root() {
  const compareAtom = useRecoilValue(compareState);
  useEffect(()=>{},[compareState]);

  return (
    <div style={{display: 'flex', flexDirection: 'column', justifyContent: 'center', alignItems: 'center'}}>
      <Header />
      <Navbar />
      <Navigation/>
      <Outlet /> 
      {compareAtom.length>0 && <CompareList/>}
      <Footer />
    </div>
  );
}

