import React, {Suspense} from "react";
import {BrowserRouter, Route, Routes} from "react-router-dom";
import AllExpenses from "./AllExpenses";
import EditExpense from "./EditExpense";
import {AllByRole} from "@testing-library/react";

function App() {

    function nö() {}

    return (
        <div>
            <Suspense fallback={"Loading..."}>
            <BrowserRouter>

                    <Routes>
                        <Route path={'/'} element={<AllExpenses />}/>
                        <Route path={'/expenses'} element={<AllExpenses />}/>
                        <Route path={'/edit'} element={<EditExpense onItemCreation={nö}/>}/>
                    </Routes>

            </BrowserRouter>
            </Suspense>
        </div>
    );
}

export default App;
