import React, {useCallback, useState} from 'react';
import { useDropzone } from 'react-dropzone';
import {ReactComponent as SearchLogo} from './iconmonstr-search-thin.svg';
import {ReactComponent as Upload} from './iconmonstr-upload-17.svg';
import {ReactComponent as Drop} from './iconmonstr-arrow-65.svg';
import { getCVDetails, search, uploadCV } from './services';

function App() {
  const [file, setFile] = useState<File>();
  const [searchInput, setSearchInput] = useState<string>('');
  const [searchResult, setSearchResult] = useState<CVModel[]>([]);
  const handleSearch = useCallback(e => {
    e.preventDefault();
    search(searchInput)
      .then((res) => {
        setSearchResult(res.data);
      })
  }, [searchInput]);
  const handleClick = useCallback((id: string) => {
    getCVDetails(id)
      .then((res) => { window.location.href = res.data.url; })
      .catch(console.error);
  }, []);
  const handleUpload = useCallback(() => {
    if (file)
      uploadCV(file)
        .then(res => { 
            alert(`${res.data.id} uploaded`);
            setFile(undefined);
         })
        .catch(console.error);
  }, [file]);
  const onDrop = useCallback(acceptedFiles => {
    setFile(acceptedFiles[0]);
  }, [])
  const {getRootProps, getInputProps, isDragActive} = useDropzone({onDrop})

  return (
    <div className="app">
      <div className="navbar">
        <li id="navbar-list">
          <a href="https://github.com/alexandre-em/CVIndexing"><ul>Code</ul></a>
          <a href="http://localhost:5601"><ul>Kibana</ul></a>
          <a href="http://localhost:8080/apidocs.html"><ul>OpenAPI</ul></a>
        </li>
      </div>
      <div className="search-upload">
        <div className="search-result">
          <div className="search-box">
            <form className="searchbar" onSubmit={handleSearch} >
              <input type="search" name="search" value={searchInput} id="search" placeholder="Search a CV..." onChange={(e) => setSearchInput(e.target.value)}/>
              <SearchLogo onClick={handleSearch}/>
            </form>
          </div>
          <div className="result">
            {searchResult.length > 0 && <h2 style={{ width: "80%" }}>Results "{searchInput}":</h2>}
            {searchResult.map((val:CVModel) => {
              return (
              <div key={`cv${val.id}`} className="cv-result" onClick={() => { handleClick(val.id) }}>
                <p><b>CV ID: {val.id}</b><br/>
                type: {val.type}<br/>
                Upload date: {new Date(parseInt(val.uploadedDate)).toUTCString()}
                </p>
              </div>
              )})}
          </div>
        </div>
        <div className="upload-zone">
          <div className="drag-and-drop">
            <div className="drop-zone" {...getRootProps()}>
              <input {...getInputProps()} />
              {isDragActive? <Drop />:<Upload />}
              <p style={{ marginTop: 15 }}>
                {file?.name}
              </p>
            </div>
            <button id="upload-button" disabled={file===undefined} onClick={handleUpload} >Upload</button>
          </div>
        </div>
      </div>
    </div>
  )}

export default App;
