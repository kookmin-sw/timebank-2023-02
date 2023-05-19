import { useState } from "react";

export interface Pagination {
    page: number;
    size: number;
    setPage: (page: number) => void;
    setSize: (size: number) => void;
    resetPagination: () => void;
}

export function usePagination(defaultPageSize: number = 20): Pagination {
    const [page, setPage] = useState(1);
    const [size, setSize] = useState(defaultPageSize);

    const resetPagination = () => {
        setPage(1);
        setSize(defaultPageSize);
    };

    return {
        page,
        size,
        setPage,
        setSize,
        resetPagination,
    };
}