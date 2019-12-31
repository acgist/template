
/**
 * @param path 路径
 * @returns 是否是连接
 */
export function isExternal(path) {
  return /^(https?:|mailto:|tel:)/.test(path)
}
