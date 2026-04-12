/**
 * 数据导出工具
 */

/**
 * 将数据转换为CSV格式
 */
export function convertToCSV(data: any[], columns: string[]): string {
  if (!data || data.length === 0) return ''

  // CSV头部
  const headers = columns.join(',')

  // CSV数据行
  const rows = data.map((row) => {
    return columns
      .map((col) => {
        const value = row[col]
        // 处理包含逗号或引号的值
        const stringValue = value != null ? String(value) : ''
        if (stringValue.includes(',') || stringValue.includes('"') || stringValue.includes('\n')) {
          return `"${stringValue.replace(/"/g, '""')}"`
        }
        return stringValue
      })
      .join(',')
  })

  // 添加BOM以支持中文
  return '\uFEFF' + [headers, ...rows].join('\n')
}

/**
 * 下载文件
 */
export function downloadFile(content: string, filename: string, mimeType: string = 'text/csv') {
  const blob = new Blob([content], { type: mimeType })
  const url = URL.createObjectURL(blob)
  const link = document.createElement('a')
  link.href = url
  link.download = filename
  document.body.appendChild(link)
  link.click()
  document.body.removeChild(link)
  URL.revokeObjectURL(url)
}

/**
 * 导出为CSV文件
 */
export function exportToCSV(
  data: any[],
  columns: string[],
  filename: string = `export_${new Date().getTime()}.csv`
) {
  const csvContent = convertToCSV(data, columns)
  downloadFile(csvContent, filename, 'text/csv;charset=utf-8;')
}

/**
 * 导出JSON文件
 */
export function exportToJSON(
  data: any[],
  filename: string = `export_${new Date().getTime()}.json`
) {
  const jsonContent = JSON.stringify(data, null, 2)
  downloadFile(jsonContent, filename, 'application/json')
}

/**
 * 从表格数据导出（支持Element Plus表格列配置）
 */
export function exportTableData(
  data: any[],
  columns: Array<{ prop: string; label: string }>,
  filename?: string
) {
  const columnProps = columns.map((col) => col.prop)
  const finalFilename = filename || `export_${new Date().getTime()}.csv`
  exportToCSV(data, columnProps, finalFilename)
}
