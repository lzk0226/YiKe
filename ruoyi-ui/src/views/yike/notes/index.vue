<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="笔记标题" prop="title">
        <el-input
          v-model="queryParams.title"
          placeholder="请输入笔记标题"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="学科分类ID" prop="subjectId">
        <el-input
          v-model="queryParams.subjectId"
          placeholder="请输入学科分类ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="笔记类型ID" prop="noteTypeId">
        <el-input
          v-model="queryParams.noteTypeId"
          placeholder="请输入笔记类型ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="作者ID" prop="userId">
        <el-input
          v-model="queryParams.userId"
          placeholder="请输入作者ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="浏览量" prop="views">
        <el-input
          v-model="queryParams.views"
          placeholder="请输入浏览量"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="点赞数" prop="likes">
        <el-input
          v-model="queryParams.likes"
          placeholder="请输入点赞数"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="收藏数" prop="favorites">
        <el-input
          v-model="queryParams.favorites"
          placeholder="请输入收藏数"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="评分(0-5分)" prop="rating">
        <el-input
          v-model="queryParams.rating"
          placeholder="请输入评分(0-5分)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="评分人数" prop="ratingCount">
        <el-input
          v-model="queryParams.ratingCount"
          placeholder="请输入评分人数"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="创建时间" prop="createdAt">
        <el-date-picker clearable
                        v-model="queryParams.createdAt"
                        type="date"
                        value-format="yyyy-MM-dd"
                        placeholder="请选择创建时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="更新时间" prop="updatedAt">
        <el-date-picker clearable
                        v-model="queryParams.updatedAt"
                        type="date"
                        value-format="yyyy-MM-dd"
                        placeholder="请选择更新时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['yike:notes:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['yike:notes:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['yike:notes:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['yike:notes:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="notesList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="数据id" align="center" prop="id" />
      <el-table-column label="笔记标题" align="center" prop="title" />
      <el-table-column label="笔记内容预览" align="center" prop="content" width="200">
        <template slot-scope="scope">
          <el-button
            type="text"
            size="small"
            @click="handleViewContent(scope.row)"
          >
            查看内容
          </el-button>
        </template>
      </el-table-column>
      <el-table-column label="笔记简介" align="center" prop="description" />
      <el-table-column label="学科分类ID" align="center" prop="subjectId" />
      <el-table-column label="笔记类型ID" align="center" prop="noteTypeId" />
      <el-table-column label="作者ID" align="center" prop="userId" />
      <el-table-column label="浏览量" align="center" prop="views" />
      <el-table-column label="点赞数" align="center" prop="likes" />
      <el-table-column label="收藏数" align="center" prop="favorites" />
      <el-table-column label="评分(0-5分)" align="center" prop="rating" />
      <el-table-column label="评分人数" align="center" prop="ratingCount" />
      <el-table-column label="创建时间" align="center" prop="createdAt" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createdAt, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="更新时间" align="center" prop="updatedAt" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updatedAt, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态: 1-正常, 0-下架" align="center" prop="status" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['yike:notes:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['yike:notes:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 查看富文本内容对话框 -->
    <el-dialog title="笔记内容" :visible.sync="contentDialogVisible" width="800px" append-to-body>
      <div class="rich-text-content" v-html="currentContent"></div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="contentDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 添加或修改笔记对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="笔记标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入笔记标题" />
        </el-form-item>
        <el-form-item label="笔记内容" prop="content">
          <editor v-model="form.content" :min-height="400"/>
        </el-form-item>
        <el-form-item label="笔记简介" prop="description">
          <el-input v-model="form.description" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="学科分类ID" prop="subjectId">
          <el-input v-model="form.subjectId" placeholder="请输入学科分类ID" />
        </el-form-item>
        <el-form-item label="笔记类型ID" prop="noteTypeId">
          <el-input v-model="form.noteTypeId" placeholder="请输入笔记类型ID" />
        </el-form-item>
        <el-form-item label="作者ID" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入作者ID" />
        </el-form-item>
        <el-form-item label="浏览量" prop="views">
          <el-input v-model="form.views" placeholder="请输入浏览量" />
        </el-form-item>
        <el-form-item label="点赞数" prop="likes">
          <el-input v-model="form.likes" placeholder="请输入点赞数" />
        </el-form-item>
        <el-form-item label="收藏数" prop="favorites">
          <el-input v-model="form.favorites" placeholder="请输入收藏数" />
        </el-form-item>
        <el-form-item label="评分(0-5分)" prop="rating">
          <el-input v-model="form.rating" placeholder="请输入评分(0-5分)" />
        </el-form-item>
        <el-form-item label="评分人数" prop="ratingCount">
          <el-input v-model="form.ratingCount" placeholder="请输入评分人数" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">正常</el-radio>
            <el-radio :label="0">下架</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="创建时间" prop="createdAt">
          <el-date-picker clearable
                          v-model="form.createdAt"
                          type="date"
                          value-format="yyyy-MM-dd"
                          placeholder="请选择创建时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="更新时间" prop="updatedAt">
          <el-date-picker clearable
                          v-model="form.updatedAt"
                          type="date"
                          value-format="yyyy-MM-dd"
                          placeholder="请选择更新时间">
          </el-date-picker>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listNotes, getNotes, delNotes, addNotes, updateNotes } from "@/api/yike/notes"

export default {
  name: "Notes",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 笔记表格数据
      notesList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 富文本内容对话框
      contentDialogVisible: false,
      // 当前查看的富文本内容
      currentContent: '',
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        title: null,
        content: null,
        description: null,
        subjectId: null,
        noteTypeId: null,
        userId: null,
        views: null,
        likes: null,
        favorites: null,
        rating: null,
        ratingCount: null,
        createdAt: null,
        updatedAt: null,
        status: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        title: [
          { required: true, message: "笔记标题不能为空", trigger: "blur" }
        ],
        content: [
          { required: true, message: "笔记内容(富文本)不能为空", trigger: "blur" }
        ],
        subjectId: [
          { required: true, message: "学科分类ID不能为空", trigger: "blur" }
        ],
        noteTypeId: [
          { required: true, message: "笔记类型ID不能为空", trigger: "blur" }
        ],
        userId: [
          { required: true, message: "作者ID不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询笔记列表 */
    getList() {
      this.loading = true
      listNotes(this.queryParams).then(response => {
        this.notesList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    /** 查看富文本内容 */
    handleViewContent(row) {
      this.currentContent = row.content || '暂无内容'
      this.contentDialogVisible = true
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        title: null,
        content: null,
        description: null,
        subjectId: null,
        noteTypeId: null,
        userId: null,
        views: null,
        likes: null,
        favorites: null,
        rating: null,
        ratingCount: null,
        createdAt: null,
        updatedAt: null,
        status: null
      }
      this.resetForm("form")
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm")
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加笔记"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getNotes(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改笔记"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateNotes(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addNotes(this.form).then(response => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal.confirm('是否确认删除笔记编号为"' + ids + '"的数据项?').then(function() {
        return delNotes(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('yike/notes/export', {
        ...this.queryParams
      }, `notes_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>

<style scoped>
.rich-text-content {
  padding: 20px;
  max-height: 600px;
  overflow-y: auto;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  background-color: #fff;
}

.rich-text-content >>> img {
  max-width: 100%;
  height: auto;
  display: block;
  margin: 10px 0;
}

.rich-text-content >>> p {
  margin: 10px 0;
  line-height: 1.6;
}

.rich-text-content >>> h1,
.rich-text-content >>> h2,
.rich-text-content >>> h3 {
  margin: 15px 0 10px;
}
</style>
