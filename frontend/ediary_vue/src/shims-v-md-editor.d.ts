declare module '@kangc/v-md-editor' {
  interface VMdEditorType {
    use: (plugin: any, options?: any) => void;
    lang: {
      use: (lang: string, config: any) => void;
    };
  }

  const VMdEditor: VMdEditorType;

  export default VMdEditor;
};
declare module '@kangc/v-md-editor/lib/theme/github.js';
declare module '@kangc/v-md-editor/lib/lang/ru-RU';
