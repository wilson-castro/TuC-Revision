export default class SearchParams {
  private searchParamsObject: { [x: string]: string } = {};
  constructor() {
    if (typeof window !== "undefined") {
      const searchParams = new URLSearchParams(window.location.search);

      searchParams.forEach((value, key) => {
        this.searchParamsObject[key] = value;
      });
    }
  }
  setSearchParam(name: string, value: string) {
    const url = new URL(window.location.href);
    url.searchParams.set(name, value);
    window.history.pushState({}, "", url);
  }
  getSearchParam(name: string) {
    return this.searchParamsObject[name];
  }
  getSearchParams() {
    return this.searchParamsObject;
  }
  clearSearchParams() {
    const url = new URL(window.location.href);
    url.searchParams.forEach((_, key) => url.searchParams.delete(key));
    window.history.pushState({}, "", url);
  }
  deleteSearchParam(name: string) {
    const url = new URL(window.location.href);
    url.searchParams.delete(name);
    window.history.pushState({}, "", url);
  }
}
