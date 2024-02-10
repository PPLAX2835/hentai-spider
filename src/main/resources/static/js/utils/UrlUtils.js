/**
 * 从url中获得请求参数
 * @param url
 * @returns {{[p: string]: string}}
 */
function getUrlParams(url) {
    let urlStr = url.split('?')[1]
    const urlSearchParams = new URLSearchParams(urlStr)
    const result = Object.fromEntries(urlSearchParams.entries())
    return result
}