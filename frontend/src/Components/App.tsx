import React, {Suspense} from "react";
import {BrowserRouter, Route, Routes} from "react-router-dom";
import AllExpenses from "./AllExpenses";
import EditExpense from "./EditExpense";
import CreateExpense from "./CreateExpense";

function App() {

    return (
        <div>
            <Suspense fallback={"Loading..."}>
            <BrowserRouter>

                    <Routes>
                        <Route path={'/'} element={<AllExpenses />}/>
                        <Route path={'/expenses'} element={<AllExpenses />}/>
                        <Route path={'/edit'} element={<CreateExpense />}/>
                        <Route path={'/edit/:expenseId'} element={<EditExpense />}/>
                    </Routes>

            </BrowserRouter>
            </Suspense>
        </div>
    );
}

export default App;
