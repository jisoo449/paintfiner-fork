import { createBrowserRouter, Route, RouterProvider } from "react-router-dom";
import Home from "./pages/Home";
import NotFound from "./pages/NotFound";
import Root from "./pages/Root";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import Search from "./pages/Search";
import PaintDetail from "./pages/PaintDetail";
import MyPalette from "./pages/MyPalette";
import MyPaletteDetail from "./pages/MyPaletteDetail";
import Login from "./pages/Login";
import Join from "./pages/Join";
import ApiTest from "./pages/ApiTest";
import Forum from "./pages/Forum";
import LoginHandler from "./pages/LoginHandler";
import Health from "./pages/Health";
import Mypage from "./pages/Mypage";
const queryClient = new QueryClient();

const router = createBrowserRouter([
  {
    path: "/",
    element: <Root />,
    errorElement: <NotFound />,
    children: [
      { index: true, element: <Home /> },
      { path: "/:mainCate", element: <Search /> },
      { path: "/:mainCate/:cate1", element: <Search /> },
      { path: "/:mainCate/:cate1/:cate2", element: <Search /> },
      { path: "/paintDetail/:paintIdx", element: <PaintDetail /> },
      { path: "/paintDetail/:cate1/:paintIdx", element: <PaintDetail /> },
      { path: "/paintDetail/:cate1/:cate2/:paintIdx", element: <PaintDetail /> },
      { path: "/mypalette", element: <MyPalette /> },
      { path: "/mypalette/:pal_idx_pk", element: <MyPaletteDetail /> },
      { path: "/login", element: <Login /> },
      { path: "/join", element: <Join /> },
      { path: "/apitest", element: <ApiTest /> },
      { path: "/forum", element: <Forum /> },
      { path: "/logincallback", element: <LoginHandler /> },
      { path: "/health", element: <Health /> },
      { path: "/mypage", element: <Mypage /> },
    ],
  },
]);

function App() {

  return (
    <QueryClientProvider client={queryClient}>
      <RouterProvider router={router} />
    </QueryClientProvider>
  );
}

export default App;
//<ReactQueryDevtools initialIsOpen={true} />
